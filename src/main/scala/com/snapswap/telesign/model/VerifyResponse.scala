package com.snapswap.telesign.model

import com.snapswap.telesign.TelesignError

/**
  * An object that describes aspects of the user's phone.
  *
  * @param phoneNumber     The user's phone number, prefixed with the Country Dialing Code.
  * @param operatingSystem The name of the mobile operating system running on the phone.
  * @param language        The IETF Language Tag corresponding to the user's written language,
  *                        as they have configured it on their phone (in Language Setting).
  */
private[telesign] case class Device(phoneNumber: String,
                  operatingSystem: String,
                  language: String)

/**
  * An object that describes aspects of the user's phone.
  *
  * @param signature    The TeleSign-assigned ID associated with your whitelabel app.
  * @param createdOnUtc A timestamp value indicating when your whitelabel app was activated by TeleSign.
  */
private[telesign] case class App(signature: String,
               createdOnUtc: String)

private[telesign] object EnumCallForwardAction extends Enumeration {
  type CallForwardAction = Value
  val FLAG = Value("FLAG")
  val BLOCK = Value("BLOCK")
}

private[telesign] object EnumCallForward extends Enumeration {
  type CallForward = Value
  val FORWARDED, NOT_FORWARDED, UNAVAILABLE, UNSUPPORTED = Value
}

/**
  * An object that describes the Call orwarding status
  *
  * @param action      A string value that indicates FLAG or BLOCK
  * @param callForward Returns the following values: FORWARDED, NOT FORWARDED, UNAVAILABLE and UNSUPPORTED..
  */
private[telesign] case class CallForwarding(action: EnumCallForwardAction.CallForwardAction,
                          callForward: EnumCallForward.CallForward)

private[telesign] object EnumUserResponseSelection extends Enumeration {
  type UserResponseSelection = Value
  val ALLOWED = Value("ALLOWED")
  val DENIED = Value("DENIED")
  val REPORTED_FRAUD = Value("REPORTED_FRAUD")
}

/**
  * An object that describes the user's verification response.
  *
  * @param received         A timestamp marking the time when TeleSign received the user's verification response.
  * @param verificationCode The pass code returned from the user.
  * @param selection        Indicates the user's intention, as selected from three choices.
  *                         Possible values are ALLOWED, DENIED, and REPORTED_FRAUD.
  */
private[telesign] case class UserResponse(received: String,
                        verificationCode: String,
                        selection: EnumUserResponseSelection.UserResponseSelection
                       )

private[telesign] object EnumVerifyCodeState extends Enumeration {
  type VerifyCodeState = Value
  val VALID, INVALID, UNKNOWN = Value
}

/**
  * An object that describes the verification status.
  *
  * @param codeState    Indicates whether the verification code entered matches that which was sent. Possible values are VALID, INVALID, or UNKNOWN.
  *                     When the code entered matches the code sent, the response will be VALID.
  *                     When the code entered does not match the code sent, code_state will be INVALID.
  * @param codeEntered  Always set to an empty string.
  * @param codeExpected In case of code challenge, contains the verification code presented to the user otherwise contains null if Simple push verification.
  */
private[telesign] case class Verify(codeState: EnumVerifyCodeState.VerifyCodeState,
                  codeEntered: Option[String],
                  codeExpected: Option[String])

/**
  * The <strong>VerifyResponse</strong> class encapsulates all of the information returned from a call to either the <strong>Verify SMS</strong> web service, or to the <strong>Verify Call</strong>  web service.
  *
  * @param referenceId    A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response.
  * @param resourceUri    A String containing the URI for accesses the PhoneID resource.
  * @param subResource    A String containing the name of the subresource that was accessed. For example, "standard".
  * @param errors         An array of [[com.snapswap.telesign.TelesignError]] objects.
  *                       Each Error object contains information about an error condition that might have resulted from the Request.
  * @param status         An object containing details about the request status.
  * @param device         An object that describes aspects of the user's phone.
  * @param app            An object that identifies your whitelabel app (customized/branded version of our AuthID application).
  * @param callForwarding An object that describes the call forwarding status.
  * @param verify         An object that describes the verification status.
  * @param userResponse   An object that describes the user's verification response.
  * @param risk           An object that describes the risk score for the phone number specified in the request.
  */
private[telesign] case class VerifyResponse(referenceId: Option[String],
                          resourceUri: Option[String],
                          subResource: Option[String],
                          errors: Seq[TelesignError],
                          status: Status,
                          device: Option[Device],
                          app: Option[App],
                          callForwarding: Option[CallForwarding],
                          verify: Option[Verify],
                          userResponse: Option[UserResponse],
                          risk: Option[Risk])

