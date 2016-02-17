package com.snapswap.telesign

import java.net.URLEncoder
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.snapswap.telesign.model._
import org.joda.time.{DateTime, DateTimeZone}
import spray.json._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class AkkaHttpTelesignClient(customerId: String, apiKey: String, useCaseCode: EnumUseCaseCodes.UseCaseCode)
                            (implicit system: ActorSystem, materializer: Materializer) extends TelesignClient {

  import TelesignDateFormat._
  import com.snapswap.telesign.unmarshaller.UnMarshallerVerify._
  import system.dispatcher

  private val log = Logging(system, this.getClass)
  private val baseURL = "/v1"
  protected val decodedKey = Base64.getDecoder.decode(apiKey)
  protected val signingKey = new SecretKeySpec(decodedKey, "HmacSHA256")
  protected val mac = {
    val _mac = Mac.getInstance("HmacSHA256")
    _mac.init(signingKey)
    _mac
  }

  override def getScore(number: String): Future[PhoneScore] =
    send(get(s"/phoneid/score/$number?ucid=$useCaseCode")) { responseStr =>
      val response = responseStr.parseJson.convertTo[PhoneIdResponse]

      val phone: String =
        response
          .numbering
          .flatMap(_.cleansing.map {
          cleansing =>
            if (cleansing.call.cleansedCode > 101) {
              throw TelesignException(s"Can't get score for number [$number], because cleansing code [${cleansing.call.cleansedCode}] more than 101")
            } else {
              s"${cleansing.call.countryCode}${cleansing.call.phoneNumber}"
            }
        })
          .getOrElse {
            throw TelesignException(s"Can't get score for number [$number], because cleansing call number is empty")
          }

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

      PhoneScore(
        phone = phone,
        phoneType = _phoneType,
        score = _score)
    }

  override def initiateVerification(number: String, code: String): Future[PhoneVerificationId] =
    send(
      post(s"/verify/sms",
        Map(
          "phone_number" -> number,
          "language" -> "en-US",
          "template" -> code
        )
      )
    ) { responseStr =>
      val response = responseStr.parseJson.convertTo[SmsVerifyResponse]

      PhoneVerificationId(response.referenceId)
    }

  override def getVerification(id: PhoneVerificationId): Future[PhoneVerification] =
    send(get(s"/verify/${id.value}")) { responseStr =>
      val response = responseStr.parseJson.convertTo[VerifyResponse]


      PhoneVerification(id,
        response.errors,
        EnumSmsStatusCodes.withCode(response.status.code)
      )
    }

  private lazy val layerConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http()
      .outgoingConnectionHttps("rest.telesign.com", 443)
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
             |x-ts-auth-method:HMAC-SHA256
             |x-ts-date:$date
             |$dataString
             |${request.uri.path}""".stripMargin
        }
      case other =>
        Future.successful(
          s"""$method
             |$contentType
             |
             |x-ts-auth-method:HMAC-SHA256
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
              .addHeader(authMethodHeader("HMAC-SHA256"))
          )
          .via(layerConnectionFlow)
          .runWith(Sink.head)
    }
  }

  def signatureHeader(signature: String): HttpHeader =
    RawHeader("Authorization", s"TSA $customerId:$signature")

  def dateHeader(date: String): HttpHeader =
    RawHeader("x-ts-date", date)

  def authMethodHeader(method: String): HttpHeader =
    RawHeader("x-ts-auth-method", method)

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
            throw TelesignRequestError(asString.parseJson.convertTo[ErrorResponse].errors)
          }
        }.map(handler)
    }
  }

  private def get(path: String): HttpRequest = Get(baseURL + path)

  private def post(path: String, fields: Map[String, String]): HttpRequest = {
    val endpoint = baseURL + path

    log.info(s"Prepare request: POST [$endpoint] with [$fields]")

    Post(endpoint, FormData(fields))
  }
}