package com.snapswap.telesign.unmarshaller

import com.snapswap.telesign.model._
import spray.json._

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
}