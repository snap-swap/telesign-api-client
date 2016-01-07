package com.snapswap.telesign.unmarshaller

import com.snapswap.telesign.model._
import spray.json._

trait VerifyResponseUnMarshaller
  extends DefaultJsonProtocol {
  this: CommonUnMarshaller =>

  implicit val deviceFormat = jsonFormat(Device, "phone_number", "operating_system", "language")
  implicit val appFormat = jsonFormat(App, "signature", "created_on_utc")

  implicit val callForwardActionEnumFormat = enumNameFormat(EnumCallForwardAction)
  implicit val callForwardEnumFormat = enumNameFormat(EnumCallForward)
  implicit val enumUserResponseSelectionFormat = enumNameFormat(EnumUserResponseSelection)
  implicit val verifyCodeStateEnumFormat = enumNameFormat(EnumVerifyCodeState)

  implicit val callForwardingFormat = jsonFormat(CallForwarding, "action", "call_forward")
  implicit val userResponseFormat = jsonFormat(UserResponse, "received", "verification_code", "selection")
  implicit val verifyFormat = jsonFormat(Verify, "code_state", "code_entered", "code_expected")

  implicit val verifyResponseFormat = jsonFormat(VerifyResponse, "reference_id", "resource_uri", "sub_resource", "errors", "status", "device", "app", "call_forwarding", "verify", "user_response", "risk")
  implicit val smsVerifyResponseFormat = jsonFormat(SmsVerifyResponse, "reference_id", "resource_uri", "sub_resource", "errors", "status", "user_response")
}