package com.snapswap.telesign.model

import com.snapswap.telesign.TelesignError
import spray.json._
import org.scalatest._

class UnMarshallerSpec extends WordSpecLike with Matchers {

  import com.snapswap.telesign.unmarshaller.UnMarshaller$Verify._

  "Unmarshaller" should {
    "be able to parse phoneId Standart response" in {
      val result = phoneIdScoreResponse.parseJson.convertTo[PhoneIdResponse]

      result.referenceId shouldBe Some("35485E92B394131C9046279B2E6E6C2F")
      result.subResource shouldBe Some("score")
    }
    "be able to parse phoneId response when Authorization error happens" in {
      val result = phoneIdAuthorizationErrorResponse.parseJson.convertTo[PhoneIdResponse]

      result.errors shouldBe Seq(TelesignError(-30000, "Invalid Customer ID.."))
    }
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
      |      "complete_phone_number": "79206119288",
      |      "country_code": "7",
      |      "phone_number": "9206119288"
      |    },
      |    "cleansing": {
      |      "call": {
      |        "country_code": "7",
      |        "phone_number": "9206119288",
      |        "cleansed_code": 100,
      |        "min_length": 10,
      |        "max_length": 10
      |      },
      |      "sms": {
      |        "country_code": "7",
      |        "phone_number": "9206119288",
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
}