package com.snapswap.telesign.model

object EnumRisks extends Enumeration {
  type Risk = Value

  val Low = Value("low")
  val MediumLow = Value("medium-Low")
  val MediumHigh = Value("medium-High")
  val High = Value("high")
}

object EnumActions extends Enumeration {
  type Action = Value

  val Allow = Value("allow")
  val Flag = Value("flag")
  val Block = Value("block")
}

object EnumPhoneTypes extends Enumeration {
  type PhoneType = PhoneTypeValue

  import EnumRisks._
  import EnumActions._

  protected case class PhoneTypeValue(code: Int, risk: EnumRisks.Risk, action: EnumActions.Action) extends super.Val() {
    override def toString(): String =
      s"PhoneType(code [$code], risk [$risk], action [$action])"
  }

  /**
    * Fixed-line phones include traditional landline phones and VOIP phones that can be traced back to a particular address and cannot be obtained by a user outside the phone number’s designated country.
    * Because these phone numbers are traceable, they are low-risk.
    */
  val FixedLine = PhoneTypeValue(1, Low, Allow)
  /**
    * Mobile telephones are provided by companies such as Verizon, Cingular, or Sprint.
    * Users of these phones must sign contracts, making these telephone numbers traceable.
    * These phone numbers are generally low-risk. However, some prepaid mobile phones will be identified as Mobile.
    * Internationally, phones identified as mobile can also include TETRA mobile phones, cordless access systems, proprietary fixed radio access, and fixed cellular systems.
    */
  val Mobile = PhoneTypeValue(2, MediumLow, Allow)
  /**
    * These telephones can be purchased anonymously at department stores.
    * They work like other mobile phones, but their users are not contracted with a mobile phone company.
    * Prepaid mobile phones generally cost between $30 and $100, making them unattractive for most fraudsters, but TeleSign recommends flagging transactions made with this type of telephone due to users’ anonymity.
    * Also, not all prepaid mobile phones will be identified as prepaid; some will be identified as Mobile.
    */
  val PrePaidMobile = PhoneTypeValue(3, MediumHigh, Flag)
  /**
    * Toll-Free numbers are numbers starting with toll-free area codes, such as (800).
    * Although often used by legitimate users calling from companies, fraudsters can easily obtain toll-free numbers that either forward to a telephone located in another country or toll-free numbers that terminate at an Internet voicemail box.
    * Toll-free numbers always forward to non-toll-free numbers. For this reason, any legitimate user who provides a toll-free number and is asked to provide a non-toll-free number will be able to do so.
    * TeleSign therefore recommends blocking users of toll-free numbers.
    */
  val TollFree = PhoneTypeValue(4, High, Block)
  /**
    * Voice Over Internet Protocol (VOIP) phone numbers are Internet-based telephone numbers.
    * Non-fixed VOIP telephone numbers can easily be obtained by users who are not located in the country associated with the telephone number.
    * They are untraceable and disposable; some can even be obtained for free.
    * This means that a fraudster in Romania could easily obtain a US-based telephone number using a non-fixed VOIP service and receive a call to this number in his or her home in Romania.
    */
  val NonFixedVOIP = PhoneTypeValue(5, High, Block)
  /**
    * This phone number rings to a pager.
    * Because pagers cannot receive verification calls, it’s best to block calls to these numbers.
    * These calls are most likely requested by fraudsters who are inputting invalid phone numbers.
    */
  val Pager = PhoneTypeValue(6, High, Block)
  /**
    * Calls to a payphone cannot be traced back to any one person; therefore, we recommend blocking verification calls to these numbers.
    */
  val Payphone = PhoneTypeValue(7, High, Block)
  /**
    * The telephone number entered is not a valid number.
    */
  val Invalid = PhoneTypeValue(8, High, Block)
  /**
    * The telephone number entered is a restricted number.
    * In the US, restricted numbers include numbers that begin with 0 and numbers with the following area codes:
    * - 900
    * - 911
    * - 411
    *
    * Internationally, the following phone types are restricted:
    * - Audiotext
    * - Carrier selection
    * - National rate
    * - Premium rate
    * - Shared cost
    * - Satellite
    * - Short Code
    */
  val RestrictedNumber = PhoneTypeValue(9, High, Block)
  /**
    * A “Personal” phone number is a phone number which allows a Subscriber to receive calls at almost any telephone number, including a mobile number.
    * The “Personal” number forwards to another phone number, which is determined by the subscriber.
    * Because the “Personal” number can forward to either a landline or mobile number, it’s impossible to determine what type of phone the subscriber is using.
    * However, because “Personal” phone numbers must be obtained from a phone company, there is little risk of fraud associated with this phone type.
    */
  val Personal = PhoneTypeValue(10, MediumLow, Allow)
  /**
    * Voicemail phone numbers will ring directly to a user’s voicemail.
    * Although this phone number was likely purchased through a phone company and may be traceable, it will be impossible to reach the user live at this phone number.
    * In addition, any user providing a voicemail number should also have a valid landline or mobile phone available.
    * Therefore, TeleSign recommends blocking this type of phone.
    */
  val Voicemail = PhoneTypeValue(11, MediumHigh, Block)
  /**
    * Phone numbers labeled “Other” are all various types of phones that originate in non-US countries.
    * The following types of phones may be included under the “Other” category:
    * - Global title address
    * - Inbound routing code
    * - Videotext
    * - VPN
    *
    * Because clients should not be requesting calls to these phone types, we recommend blocking verification calls to these numbers.
    * Any phone number entered that falls under the “Other” category was probably either entered incorrectly or was entered by a fraudster testing various phone numbers.
    */
  val Other = PhoneTypeValue(20, MediumHigh, Block)

  def withId(id: Int): PhoneTypeValue =
    values
      .find(_.asInstanceOf[PhoneTypeValue].code == id)
      .map(_.asInstanceOf[PhoneTypeValue])
      .getOrElse(
        throw new NoSuchElementException(s"No value found for Id [$id]")
      )
}