package com.snapswap.telesign

trait TelesignAuth {
  def spec: String
  def header: String
}

case object HmacSHA256 extends TelesignAuth {
  val spec = "HmacSHA256"
  val header =  "HMAC-SHA256"
}

case object HmacSHA1 extends TelesignAuth {
  val spec = "HmacSHA1"
  val header =  "HMAC-SHA1"
}