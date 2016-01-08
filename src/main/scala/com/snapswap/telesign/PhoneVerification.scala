package com.snapswap.telesign

case class PhoneVerification(id: PhoneVerificationId,
                             errors: Seq[TelesignError],
                             status: EnumSmsStatusCodes.SmsStatusCode)