package com.snapswap.telesign

/**
  * An array of property-value pairs, that contain information on error conditions that might have resulted from the Request.
  *
  * @param code        A 1 to 5-digit error code (possibly negative) that indicates the type of error that occurred.
  *                    When no error occurs, the default value 0 is returned.
  * @param description A string that describes the type of error that occurred. If no error occurs, this parameter is empty.
  */
case class TelesignError(code: Int,
                         description: String)