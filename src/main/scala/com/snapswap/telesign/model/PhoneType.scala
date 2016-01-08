package com.snapswap.telesign.model

/**
  * An object containing details about the phone type.
  *
  * @param code        One of the <emphasis>Phone Type Codes</emphasis>.
  * @param description A description of the phone type.
  */
private[telesign] case class PhoneType(code: String,
                     description: String)