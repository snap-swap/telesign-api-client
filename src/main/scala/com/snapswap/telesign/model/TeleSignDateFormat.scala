package com.snapswap.telesign.model

import org.joda.time.format.DateTimeFormat

private[telesign] object TelesignDateFormat {
  val PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z"
  val dateFormat = DateTimeFormat.forPattern(PATTERN)
}