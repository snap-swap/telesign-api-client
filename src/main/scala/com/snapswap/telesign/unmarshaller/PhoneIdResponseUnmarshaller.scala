package com.snapswap.telesign.unmarshaller

import scala.util.{Try, Success, Failure}
import spray.json._
import com.snapswap.telesign.{PhoneScore, TelesignInvalidPhoneNumber, EnumPhoneTypes, TelesignRequestFailure}
import com.snapswap.telesign.model._

trait PhoneIdResponseUnMarshaller
  extends DefaultJsonProtocol {
  this: CommonUnMarshaller =>

  implicit val phoneTypeFormat = jsonFormat2(PhoneType)
  implicit val originalNumberFormat = jsonFormat(OriginalNumber, "phone_number", "complete_phone_number", "country_code")
  implicit val numberFormat = jsonFormat(Number, "phone_number", "country_code", "min_length", "max_length", "cleansed_code")
  implicit val cleansingNumberFormat = jsonFormat2(CleansingNumber)
  implicit val numberingFormat = jsonFormat2(Numbering)
  implicit val countryFormat = jsonFormat3(Country)
  implicit val timeZoneFormat = jsonFormat(TimeZone, "utc_offset_min", "name", "utc_offset_max")
  implicit val coordinatesFormat = jsonFormat2(Coordinates)
  implicit val carrierFormat = jsonFormat1(Carrier)
  implicit val locationFormat = jsonFormat(Location, "county", "city", "state", "zip", "country", "time_zone", "coordinates", "metro_code")
  implicit val phoneIdResponseFormat = jsonFormat(PhoneIdResponse, "reference_id", "resource_uri", "sub_resource", "errors", "phone_type", "signature_string", "status", "numbering", "location", "carrier", "risk")

  implicit val phoneScoreReader = new RootJsonReader[PhoneScore] {
    override def read(json: JsValue) = {
      val response: PhoneIdResponse = phoneIdResponseFormat.read(json)
      if (response.errors.nonEmpty) {
        throw TelesignRequestFailure(response.errors)
      } else {
        val phone: String =
          response
            .numbering
            .flatMap(_.cleansing.map {
              cleansing =>
                TelesignInvalidPhoneNumber(cleansing.call.cleansedCode) match {
                  case Some(ex) => throw ex
                  case None => s"${cleansing.call.countryCode}${cleansing.call.phoneNumber}"
                }
            }).getOrElse {
            throw TelesignInvalidPhoneNumber("E.164 phone number is not detected")
          }

        val _score: Int = response.risk.map(_.score).getOrElse(1000) // 1000 is the highest risk value, see http://docs.telesign.com/rest/content/xt/xt-score.html#xref-score
        val _phoneType = response
            .phoneType
            .map {
              case pt =>
                Try(EnumPhoneTypes.withId(pt.code.toInt)) match {
                  case Success(ptt) =>
                    ptt
                  case Failure(ex) =>
                    EnumPhoneTypes.Other
                }
            }.getOrElse(EnumPhoneTypes.Other)

        PhoneScore(
          phone = phone,
          phoneType = _phoneType,
          score = _score)
      }
    }
  }
}
