package com.snapswap.telesign.model

import com.snapswap.telesign.TelesignError

/**
  * An object containing details about the original phone number passed to TeleSign PhoneID API.
  *
  * @param phoneNumber         The Base Phone Number.
  *                            This is simply the phone number without the Country Dialing Code.
  * @param completePhoneNumber The Base Phone Number prefixed with the Country Dialing Code.
  *                            This forms the Subresource Identifier part of the PhoneID Contact web service URI.
  * @param countryCode         A 1, 2, or 3-digit number representing the Country Dialing Code.
  *                            For example, the Country Dialing Code for both the U.S.A. and Canada is 1, and the Country Dialing Code for the United Kingdom is 44.
  */
private[telesign] case class OriginalNumber(phoneNumber: String,
                          completePhoneNumber: String,
                          countryCode: String)

/**
  * An object containing cleansing details about a phone number.
  *
  * @param phoneNumber  The Base Phone Number. This is simply the phone number without the Country Dialing Code.
  * @param countryCode  A 1, 2, or 3-digit number representing the Country Dialing Code.
  *                     For example, the Country Dialing Code for both the U.S.A. and Canada is 1, and the Country Dialing Code for the United Kingdom is 44.
  * @param minLength    The minimum number of digits allowed for phone numbers with this particular Country Dialing Code.
  * @param maxLength    The maximum number of digits allowed for phone numbers with this particular Country Dialing Code.
  * @param cleansedCode One of the Phone Number Cleansing Codes describing the cleansing operation TeleSign performed on the phone number.
  *                     The default value is 100 (No changes were made to the phone number).
  */
private[telesign] case class Number(phoneNumber: String,
                  countryCode: String,
                  minLength: Int,
                  maxLength: Int,
                  cleansedCode: Int)

/**
  * An object containing details about how the phone number was cleansed.
  * Phone Cleansing corrects common formatting issues in submitted phone numbers.
  *
  * @param sms  An object containing cleansing details about a phone number used for receiving text messages.
  * @param call An object containing cleansing details about a phone number used for receiving voice calls.
  */
private[telesign] case class CleansingNumber(sms: Number,
                           call: Number)

/**
  * An object containing details about the numbering attributes of the specified phone number.
  *
  * @param original  An object containing details about the original phone number passed to TeleSign's PhoneID API.
  * @param cleansing An object containing details about how the phone number was cleansed.
  *                  Phone Cleansing corrects common formatting issues in submitted phone numbers.
  */
private[telesign] case class Numbering(original: Option[OriginalNumber],
                     cleansing: Option[CleansingNumber])

/**
  * An object containing details about the country associated with the phone number.
  *
  * @param iso2 The ISO 3166-1 2-letter Country Code associated with the phone number.
  * @param iso3 The ISO 3166-1 3-letter Country Code associated with the phone number
  * @param name The Country Name associated with phone number.
  */
private[telesign] case class Country(iso2: String,
                   iso3: String,
                   name: String)

/**
  * An object containing details about the Time Zone associated with the phone number.
  *
  * @param utcOffsetMin For U.S. domestic phone numbers, this parameter returns the UTC offset associated with the phone number.
  * @param name         A string identifying the Time Zone Name (TZ) associated with the phone number (U.S. only). For example: "America/Los_Angeles".
  * @param utcOffsetMax For international phone numbers, this parameter returns the maximum UTC offset for the country associated with the phone number. For U.S. domestic phone numbers, this parameter returns the same result as utc_offset_min.
  */
private[telesign] case class TimeZone(utcOffsetMin: Option[String],
                    name: Option[String],
                    utcOffsetMax: Option[String])

/**
  * An object containing details about the geographical coordinates of the location where the phone number is registered.
  *
  * @param latitude  A value indicating the number of degrees of latitude of the location associated with the phone number, expressed in seven decimal digits, with five decimal places.
  *                  For example, 34.18264.
  * @param longitude A value indicating the number of degrees of longitude of the location associated with the phone number, expressed in eight decimal digits, with five decimal places F
  *                  or example, -118.30840.
  */
private[telesign] case class Coordinates(latitude: Option[Double],
                       longitude: Option[Double])

/**
  * An object containing information about the company that provides telecommunications services for the phone number.
  *
  * @param name The string specifying the name of the carrier.  For example: "Verizon".
  */
private[telesign] case class Carrier(name: String)

/**
  * An object containing geographical location information associated with the phone number.
  * <strong>Note:</strong> <emphasis>Location</emphasis> refers to the place where the phone is registered, <emphasis>not</emphasis> to where it actually is at the time of this request.
  *
  * @param county      A string specifying the name of the County (or Parish) associated with the phone number (U.S. only).
  * @param city        A string specifying the name of the city associated with the phone number.
  * @param state       The 2-letter State Code of the state (province, district, or territory) associated with the phone number (North America only).
  * @param zip         The 5-digit United States Postal Service ZIP Code associated with the phone number (U.S. only).
  * @param country     An object containing details about the country associated with the phone number.
  * @param timeZone    An object containing details about the Time Zone associated with the phone number.
  * @param coordinates An object containing details about the geographical coordinates of the location where the phone number is registered.
  * @param metroCode   A 4-digit string indicating the Primary Metropolitan Statistical Area (PMSA) Code for the location associated with the phone number (U.S. only). PMSA Codes are governed by the US Census Bureau.
  */
private[telesign] case class Location(county: Option[String],
                    city: Option[String],
                    state: Option[String],
                    zip: Option[String],
                    country: Option[Country],
                    timeZone: Option[TimeZone],
                    coordinates: Option[Coordinates],
                    metroCode: Option[String])

/**
  * The <strong>PhoneIdStandardResponse</strong> class encapsulates all of the information returned from a call to the <strong>PhoneID Standard</strong> web service.
  *
  * @param referenceId     A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response.
  * @param resourceUri     A String containing the URI for accesses the PhoneID resource.
  * @param subResource     A String containing the name of the subresource that was accessed. For example, "standard".
  * @param errors          An array of { @link com.snapswap.telesign.model.TelesignError} objects. Each Error object contains information about an error condition that might have resulted from the Request.
  * @param phoneType       An object that contains details about the phone type.
  * @param signatureString A String containing the Signature, exactly as it was sent in the Request.
  *                        Unless the request contained an invalid Signature, this contains an empty string.
  * @param status          An object containing details about the request status.
  * @param numbering       An object containing details about the numbering attributes of the specified phone number.
  * @param location        An object containing geographical location information associated with the phone number.
  * @param carrier         An object containing information about the company that provides telecommunications services for the phone number.
  * @param risk            An object that describes the risk score for the phone number specified in the request.
  */
private[telesign] case class PhoneIdResponse(referenceId: Option[String],
                           resourceUri: Option[String],
                           subResource: Option[String],
                           errors: Seq[TelesignError],
                           phoneType: Option[PhoneType],
                           signatureString: Option[String],
                           status: Status,
                           numbering: Option[Numbering],
                           location: Option[Location],
                           carrier: Option[Carrier],
                           risk: Option[Risk])