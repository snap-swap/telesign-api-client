package com.snapswap.telesign

case class PhoneScore(phone: String,
                      phoneType: EnumPhoneTypes.PhoneType,
                      score: Int) {
  override def toString = s"'$phone' -> $phoneType, $score score"
}
