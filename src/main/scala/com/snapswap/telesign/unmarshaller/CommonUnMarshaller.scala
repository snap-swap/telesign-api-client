package com.snapswap.telesign.unmarshaller

import com.snapswap.telesign.model.{Risk, Status, TelesignError}
import spray.json._

trait CommonUnMarshaller extends DefaultJsonProtocol {
  protected def enumNameFormat(enum: Enumeration) = new RootJsonFormat[enum.Value] {
    def read(value: JsValue): enum.Value = value match {
      case JsString(s) => enum.withName(s)
      case x => deserializationError("Expected Enum as JsString, but got " + x)
    }

    def write(v: enum.Value): JsValue = JsString(v.toString)
  }

  implicit val errorFormat = jsonFormat2(TelesignError)

  implicit val statusFormat = jsonFormat(Status, "updated_on", "code", "description")

  implicit val riskFormat = jsonFormat3(Risk)
}