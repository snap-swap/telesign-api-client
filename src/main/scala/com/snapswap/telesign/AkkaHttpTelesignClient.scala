package com.snapswap.telesign

import java.lang.RuntimeException
import java.net.URLEncoder
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.headers.{CustomHeader, Accept, OAuth2BearerToken, Authorization}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source, Flow}
import com.snapswap.telesign.model._
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import spray.json._

import scala.concurrent.{Await, Future}
import scala.util.{Success, Try, Failure}

class AkkaHttpTelesignClient(customerId: String, apiKey: String, auth: TelesignAuth)
                            (implicit system: ActorSystem, materializer: Materializer) extends TelesignClient {

  import com.snapswap.telesign.unmarshaller.UnMarshaller$Verify._
  import system.dispatcher
  import TelesignDateFormat._

  private val log = Logging(system, this.getClass)
  private val baseURL = "/v1"
  protected val decodedKey = Base64.getDecoder.decode(apiKey)
  protected val signingKey = new SecretKeySpec(decodedKey, auth.spec)
  protected val mac = {
    val _mac = Mac.getInstance(auth.spec)
    _mac.init(signingKey)
    _mac
  }

  override def phoneIdScore(number: String, useCaseCode: EnumUseCaseCodes.UseCaseCode): Future[PhoneIdScoreResult] =
    send(get(s"/phoneid/score/$number?ucid=$useCaseCode")) { responseStr =>
      val response = responseStr.parseJson.convertTo[PhoneIdResponse]

      val phone: String =
        response
          .numbering
          .map(
            _.cleansing.map {
              cleansing =>
                s"${cleansing.call.countryCode}${cleansing.call.phoneNumber}"
            }
          ).flatten
          .getOrElse(number)

      val _score: Int = response.risk.map(_.score).getOrElse(-1)
      val _phoneType = response
        .phoneType
        .map {
          case pt =>
            Try(EnumPhoneTypes.withId(pt.code.toInt)) match {
              case Success(pt) =>
                pt
              case Failure(ex) =>
                print(ex)
                EnumPhoneTypes.Other
            }
        }
        .getOrElse(EnumPhoneTypes.Other)

      PhoneIdScoreResult(
        phone = phone,
        phoneType = _phoneType,
        score = _score)
    }

  override def smsVerify(number: String, language: String = "en-US", template: String = "Your code is $$CODE$$"): Future[String] =
    send(
      post(s"/verify/sms",
        Map(
          "phone_number" -> number,
          "language" -> language,
          "template" -> template
        )
      )
    ) { responseStr =>
      val response = responseStr.parseJson.convertTo[SmsVerifyResponse]

      response.referenceId
    }

  override def verifyInfo(referenceId: String): Future[VerifyResponse] =
    send(get(s"/verify/$referenceId")) { responseStr =>
      log.error(s"$responseStr")
      responseStr.parseJson.convertTo[VerifyResponse]
    }

  private lazy val layerConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http()
      .outgoingConnectionTls("rest.telesign.com", 443)
      .log("telesign")

  private def http(request: HttpRequest): Future[HttpResponse] = {
    val now = new DateTime(DateTimeZone.UTC)
    val date = dateFormat.print(now)

    val method = request.method match {
      case HttpMethods.GET =>
        "GET"
      case HttpMethods.POST =>
        "POST"
    }
    val contentType = request.method match {
      case HttpMethods.POST =>
        "application/x-www-form-urlencoded; charset=UTF-8"
      case other =>
        ""
    }

    val dataFuture = request.method match {
      case HttpMethods.POST =>
        Unmarshal(request.entity)
          .to[FormData].map { case formData =>
          val dataString = formData.fields.map {
            case (key, value) =>
              s"$key=${URLEncoder.encode(value, "UTF-8")}"
          }.mkString("&")

          s"""$method
             |$contentType
             |
             |x-ts-auth-method:${auth.header}
             |x-ts-date:$date
             |$dataString
             |${request.uri.path}""".stripMargin
        }
      case other =>
        Future.successful(
          s"""$method
             |$contentType
             |
             |x-ts-auth-method:${auth.header}
             |x-ts-date:$date
             |${request.uri.path}""".stripMargin
        )
    }

    dataFuture.flatMap {
      case data =>
        val rawHmac = mac.doFinal(data.getBytes())
        val signature = new String(Base64.getEncoder.encode(rawHmac))

        Source
          .single(
            request
              .addHeader(signatureHeader(signature))
              .addHeader(dateHeader(date))
              .addHeader(authMethodHeader(auth.header))
          )
          .via(layerConnectionFlow)
          .runWith(Sink.head)
    }
  }

  def signatureHeader(signature: String): HttpHeader =
    new CustomHeader() {
      override def value(): String =
        s"TSA $customerId:$signature"

      override def name(): String =
        "Authorization"
    }

  def dateHeader(date: String): HttpHeader =
    new CustomHeader() {
      override def value(): String =
        date

      override def name(): String =
        "x-ts-date"
    }

  def authMethodHeader(method: String): HttpHeader =
    new CustomHeader() {
      override def value(): String =
        method

      override def name(): String =
        "x-ts-auth-method"
    }

  private def send[T](request: HttpRequest)(handler: String => T): Future[T] = {
    http(request).flatMap { response =>
      Unmarshal(response.entity)
        .to[String]
        .map { asString =>
          if (response.status.isSuccess) {
            log.debug(s"SUCCESS ${request.method} ${request.uri} -> ${response.status} '$asString'")
            asString
          } else {
            log.warning(s"FAILURE ${request.method} ${request.uri} -> ${response.status} '$asString'")
            throw TelesignException(asString)
            //            throw asString.parseJson.convertTo[LayerException](unmarshaller.platform.layerExceptionFormat)
          }
        }
    }.map(handler)
  }

  private def get(path: String): HttpRequest = Get(baseURL + path)

  private def post(path: String, fields: Map[String, String]): HttpRequest = {
    val endpoint = baseURL + path

    log.info(s"Prepare request: POST [$endpoint] with [$fields]")

    Post(endpoint, FormData(fields))
  }
}