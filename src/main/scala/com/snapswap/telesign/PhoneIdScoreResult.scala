package com.snapswap.telesign

import com.snapswap.telesign.model.EnumPhoneTypes

case class PhoneIdScoreResult(phone: String,
                              phoneType: EnumPhoneTypes.PhoneType,
                              score: Int)