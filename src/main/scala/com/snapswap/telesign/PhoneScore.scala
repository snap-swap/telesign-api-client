package com.snapswap.telesign

/**
  * PhoneScore entity
  * @param phone - E164-formatted phone number
  * @param country - 3-letters ISO 3166-1 country code
  * @param phoneType - type of a phone number (see http://docs.telesign.com/rest/content/xt/xt-phone-type-codes.html#xref-phone-type-codes)
  * @param score - risk score (see http://docs.telesign.com/rest/content/xt/xt-score.html#xref-score)
  */
case class PhoneScore(phone: String, country: String,
                      phoneType: EnumPhoneTypes.PhoneType,
                      score: Int) {
  override def toString = s"'$phone' -> $phoneType, $score score"
}
