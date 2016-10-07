package com.snapswap.telesign.model

import org.scalatest.{FlatSpec, Matchers}
import spray.json._
import com.snapswap.telesign.{EnumPhoneTypes, TelesignError, PhoneScore, TelesignInvalidPhoneNumber}
import com.snapswap.telesign.unmarshaller.UnMarshallerVerify

class UnMarshallerSpec extends FlatSpec with Matchers {
  import UnMarshallerVerify._

  "Unmarshaller" should "be able to parse PhoneScore from a success response" in {
    val score = phoneIdScoreResponse.parseJson.convertTo[PhoneScore]
    score.phone shouldBe "79201111111"
    score.phoneType shouldBe EnumPhoneTypes.Mobile
    score.country shouldBe "RUS"
    score.score shouldBe 11
  }
  it should "be able to parse errors from a failure response" in {
    val errorResponse = phoneIdAuthorizationErrorResponse.parseJson.convertTo[ErrorResponse]
    errorResponse.errors shouldBe Seq(TelesignError(-30000, "Invalid Customer ID.."))
  }
  it should "fail with TelesignInvalidPhoneNumber the case of invalid phone number" in {
    val thrown = the [TelesignInvalidPhoneNumber] thrownBy invalidPhone.parseJson.convertTo[PhoneScore]
    thrown.getMessage shouldBe "The phone number appears to be formatted correctly, but cannot be matched to any specific area"
  }

  private val phoneIdScoreResponse =
    """{
      |  "reference_id": "35485E92B394131C9046279B2E6E6C2F",
      |  "resource_uri": null,
      |  "sub_resource": "score",
      |  "status": {
      |    "updated_on": "2016-01-06T15:53:34.239877Z",
      |    "code": 300,
      |    "description": "Transaction successfully completed"
      |  },
      |  "errors": [],
      |  "numbering": {
      |    "original": {
      |      "complete_phone_number": "79201111111",
      |      "country_code": "7",
      |      "phone_number": "9201111111"
      |    },
      |    "cleansing": {
      |      "call": {
      |        "country_code": "7",
      |        "phone_number": "9201111111",
      |        "cleansed_code": 100,
      |        "min_length": 10,
      |        "max_length": 10
      |      },
      |      "sms": {
      |        "country_code": "7",
      |        "phone_number": "9201111111",
      |        "cleansed_code": 100,
      |        "min_length": 10,
      |        "max_length": 10
      |      }
      |    }
      |  },
      |  "phone_type": {
      |    "code": "2",
      |    "description": "MOBILE"
      |  },
      |  "location": {
      |    "city": "Kaluga Region",
      |    "state": null,
      |    "zip": null,
      |    "metro_code": null,
      |    "county": null,
      |    "country": {
      |      "name": "Russia",
      |      "iso2": "RU",
      |      "iso3": "RUS"
      |    },
      |    "coordinates": {
      |      "latitude": null,
      |      "longitude": null
      |    },
      |    "time_zone": {
      |      "name": null,
      |      "utc_offset_min": "+2",
      |      "utc_offset_max": "+2"
      |    }
      |  },
      |  "carrier": {
      |    "name": "MegaFon Central Branch"
      |  },
      |  "risk": {
      |    "level": "low",
      |    "recommendation": "allow",
      |    "score": 11
      |  }
      |}""".stripMargin

  val phoneIdAuthorizationErrorResponse =
    """
      |{
      |   "status": {
      |      "updated_on": "2012-10-03T14:51:28.709526Z",
      |      "code": 501,
      |      "description": "Not authorized"
      |   },
      |   "signature_string": "",
      |   "errors": [
      |      {
      |         "code": -30000,
      |         "description": "Invalid Customer ID.."
      |      }
      |   ]
      |}
    """.stripMargin

  val phoneIdInvalidSignatureReponse =
    """
      |{
      |   "status": {
      |      "updated_on": "2012-10-03T14:51:28.709526Z",
      |      "code": 501,
      |      "description": "Not authorized"
      |   },
      |   "signature_string": "GET\n\nTue, 01 May 2012 10:09:16 -0700\nx-ts-nonce:dff0f33c-7b52-4b6a-a556-23e32ca11fe1\nv1/phoneid/standard/15555551234",
      |   "errors": [
      |      {
      |       "code": -30006,
      |       "description": "Invalid Signature."
      |      }
      |   ]
      |}
    """.stripMargin

  val invalidPhone =
    """{
      |	"reference_id": "354DE1F545D0101C904497FEBFAEC2B8",
      |	"resource_uri": null,
      |	"sub_resource": "score",
      |	"status": {
      |		"updated_on": "2016-03-15T04:16:32.289570Z",
      |		"code": 300,
      |		"description": "Transaction successfully completed"
      |	},
      |	"errors": [],
      |	"numbering": {
      |		"original": {
      |			"complete_phone_number": "3521234567",
      |			"country_code": "352",
      |			"phone_number": "1234567"
      |		},
      |		"cleansing": {
      |			"call": {
      |				"country_code": "352",
      |				"phone_number": "1234567",
      |				"cleansed_code": 103,
      |				"min_length": null,
      |				"max_length": null
      |			},
      |			"sms": {
      |				"country_code": "352",
      |				"phone_number": "1234567",
      |				"cleansed_code": 103,
      |				"min_length": null,
      |				"max_length": null
      |			}
      |		}
      |	},
      |	"phone_type": {
      |		"code": "8",
      |		"description": "INVALID"
      |	},
      |	"location": {
      |		"city": null,
      |		"state": null,
      |		"zip": null,
      |		"metro_code": null,
      |		"county": null,
      |		"country": {
      |			"name": "Luxembourg",
      |			"iso2": "LU",
      |			"iso3": "LUX"
      |		},
      |		"coordinates": {
      |			"latitude": null,
      |			"longitude": null
      |		},
      |		"time_zone": {
      |			"name": null,
      |			"utc_offset_min": null,
      |			"utc_offset_max": null
      |		}
      |	},
      |	"carrier": {
      |		"name": ""
      |	},
      |	"risk": {
      |		"level": "high",
      |		"recommendation": "block",
      |		"score": 959
      |	}
      |}""".stripMargin
}
