package com.snapswap.telesign.model

import org.joda.time.format.DateTimeFormat

object TelesignDateFormat {
  val PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z"
  val dateFormat = DateTimeFormat.forPattern(PATTERN)
}