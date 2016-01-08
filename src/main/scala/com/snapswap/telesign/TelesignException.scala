package com.snapswap.telesign

import scala.util.control.NoStackTrace

case class TelesignException(message: String) extends NoStackTrace

case class TelesignRequestError(errors: Seq[TelesignError]) extends NoStackTrace