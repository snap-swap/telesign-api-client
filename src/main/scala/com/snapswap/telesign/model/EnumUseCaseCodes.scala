package com.snapswap.telesign.model

object EnumUseCaseCodes extends Enumeration {
  type UseCaseCode = Value
  /**
    * Prevent account takeover/compromise
    */
  val ATCK = Value("ATCK")
  /**
    * Prevent bulk account creation and fraud
    */
  val BACF = Value("BACF")
  /**
    * Prevent bulk account creation and spam
    */
  val BACS = Value("BACS")
  /**
    * Prevent chargebacks
    */
  val CHBK = Value("CHBK")
  /**
    * Calendar Event
    */
  val CLDR = Value("CLDR")
  /**
    * Prevent false lead entry
    */
  val LEAD = Value("LEAD")
  /**
    * Other
    */
  val OTHR = Value("OTHR")
  /**
    * Password reset
    */
  val PWRT = Value("PWRT")
  /**
    * Prevent fake/missed reservations
    */
  val RESV = Value("RESV")
  /**
    * Prevent prescription fraud
    */
  val RXPF = Value("RXPF")
  /**
    * Shipping Notification
    */
  val SHIP = Value("SHIP")
  /**
    * Prevent telecom fraud
    */
  val TELF = Value("TELF")
  /**
    * Prevent identity theft
    */
  val THEF = Value("THEF")
  /**
    * Transaction Verification
    */
  val TRVF = Value("TRVF")
  /**
    * Unknown/prefer not to sa
    */
  val UNKN = Value("UNKN")
}