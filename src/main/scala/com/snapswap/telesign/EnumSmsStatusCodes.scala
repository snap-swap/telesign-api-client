package com.snapswap.telesign

object EnumSmsStatusCodes extends Enumeration {
  type SmsStatusCode = Value

  protected case class SmsStatusCodeValue(code: Int, text: String) extends super.Val() {
    override def toString(): String =
      s"SmsStatusCode(code [$code])"
  }

  /**
    * SMS has been delivered to the user’s phone.
    */
  val `200` = SmsStatusCodeValue(200, "Delivered to handset")
  /**
    * SMS has been delivered to the gateway. If the gateway responds with further information (including successful delivery to handset or delivery failure), the status is updated.
    */
  val `203` = SmsStatusCodeValue(203, "Delivered to gateway")
  /**
    * SMS could not be delivered to the user’s handset for an unknown reason.
    */
  val `207` = SmsStatusCodeValue(207, "Error delivering SMS to handset (reason unknown)")
  /**
    * SMS could not be delivered to the handset, due to a temporary error with the phone. For example, the phone is turned off, or there is not enough memory to store the message.
    */
  val `210` = SmsStatusCodeValue(210, "Temporary phone error")
  /**
    * SMS could not be delivered to the handset, due to a permanent error with the phone. For example, the phone is incompatible with SMS or is illegally registered on the network. This happens when a phone number is blacklisted or incorrectly provisioned.
    */
  val `211` = SmsStatusCodeValue(211, "Permanent phone error")
  /**
    * Network cannot route the message to the handset.
    */
  val `220` = SmsStatusCodeValue(220, "Gateway/network cannot route message")
  /**
    * The message was queued by the mobile provider and timed out before it could be delivered to the handset.
    */
  val `221` = SmsStatusCodeValue(221, "Message expired before delivery")
  /**
    * SMS is not supported by this phone, carrier, plan, or user.
    */
  val `222` = SmsStatusCodeValue(222, "SMS not supported")
  /**
    * TeleSign blocked the SMS before it was sent. This is due to your prior submitted request to blocklist this phone number.
    */
  val `229` = SmsStatusCodeValue(229, "Message blocked by customer request")
  /**
    * TeleSign blocked the SMS before it was sent. This can happen if the message contains spam or inappropriate material or if TeleSign believes the client is abusing the account in any way.
    */
  val `230` = SmsStatusCodeValue(230, "Message blocked by TeleSign")
  /**
    * The content of the message is not supported.
    */
  val `231` = SmsStatusCodeValue(231, "Invalid/unsupported message content")
  /**
    * The final status of the message cannot be determined.
    */
  val `250` = SmsStatusCodeValue(250, "Final status unknown")
  /**
    * The message is being sent to the SMS gateway.
    */
  val `290` = SmsStatusCodeValue(290, "Message in progress")
  /**
    * TeleSign is experiencing unusually high volume and has queued the SMS message.
    */
  val `291` = SmsStatusCodeValue(291, "Queued by TeleSign")
  /**
    * The SMS gateway has queued the message.
    */
  val `292` = SmsStatusCodeValue(292, "Queued at gateway")
  /**
    * The status of the SMS is temporarily unavailable.
    */
  val `295` = SmsStatusCodeValue(295, "Status delayed")
  /**
    * No Call, SMS, or PhoneID request was attempted.
    */
  val `500` = SmsStatusCodeValue(500, "Transaction not attempted")
  /**
    * No permissions for this resource, or authorization failed.
    */
  val `501` = SmsStatusCodeValue(501, "Not authorized")
  /**
    * There is a problem with the short code used.
    */
  val `502` = SmsStatusCodeValue(502, "Campaign error")
  /**
    * A temporary error on the carrier or operator side. The message can be retried.
    */
  val `503` = SmsStatusCodeValue(503, "Carrier rejected - temporary problem")
  /**
    * A permanent error on the carrier or operator side. The message should not be retried.
    */
  val `504` = SmsStatusCodeValue(504, "Carrier rejected - permanent error")
  /**
    * A temporary error on TeleSign’s partner side. The message can be retried.
    */
  val `505` = SmsStatusCodeValue(505, "Error on gateway - temporary error")
  /**
    * A permanent error on TeleSign’s partner side. The message should not be retried.
    */
  val `506` = SmsStatusCodeValue(506, "Error on gateway - permanent error")
  /**
    * There is a problem with destination address used. The format is not valid, or the number is not associated with a carrier, or the MSC used does not know about the MSISDN.
    */
  val `507` = SmsStatusCodeValue(507, "Invalid destination address")
  /**
    * The message requires a source address, verify one is provided, and is correct.
    */
  val `508` = SmsStatusCodeValue(508, "Invalid source address")
  /**
    * One or more parameters used in the request is not supported.
    */
  val `509` = SmsStatusCodeValue(509, "Parameters problem")
  /**
    * End user has blocked receiving SMS by this carrier plan, or the request or the short code used is blocked.
    */
  val `510` = SmsStatusCodeValue(510, "Message blocked by subscriber action or request")
  /**
    * End user has exceeded their spending limit, and therefore cannot receive SMS.
    */
  val `511` = SmsStatusCodeValue(511, "Subscriber low on credit")
  /**
    * End user cannot receive SMS while roaming.
    */
  val `512` = SmsStatusCodeValue(512, "Roaming error")
  /**
    * SMS failed because ported combinations are unreachable.
    */
  val `513` = SmsStatusCodeValue(513, "Mobile number portability error")
  /**
    * Subscriber is absent for a period of time.
    */
  val `514` = SmsStatusCodeValue(514, "Subscriber absent")
  /**
    * This message is considered as spam by the carrier or operator.
    */
  val `515` = SmsStatusCodeValue(515, "Suspected spam")
  /**
    * The system is unable to provide a status at this time.
    */
  val `599` = SmsStatusCodeValue(599, "Status not available")

  def withCode(code: Int): SmsStatusCode =
    values
      .find(_.asInstanceOf[SmsStatusCodeValue].code == code)
      .map(_.asInstanceOf[SmsStatusCodeValue])
      .getOrElse(
        throw new NoSuchElementException(s"No value found for code [$code]")
      )
}