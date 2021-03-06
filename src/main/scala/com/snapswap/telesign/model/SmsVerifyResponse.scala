package com.snapswap.telesign.model

import com.snapswap.telesign.TelesignError

/**
  * The <strong>VerifyResponse</strong> class encapsulates all of the information returned from a call to either the <strong>Verify SMS</strong> web service, or to the <strong>Verify Call</strong>  web service.
  *
  * @param referenceId  A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response.
  * @param resourceUri  A String containing the URI for accesses the PhoneID resource.
  * @param subResource  A String containing the name of the subresource that was accessed. For example, "standard".
  * @param errors       An array of [[com.snapswap.telesign.TelesignError]] objects.
  *                     Each Error object contains information about an error condition that might have resulted from the Request.
  * @param status       An object containing details about the request status.
  * @param userResponse An object that describes the user's verification response.
  */
private[telesign] case class SmsVerifyResponse(referenceId: String,
                             resourceUri: String,
                             subResource: String,
                             errors: Seq[TelesignError],
                             status: Status,
                             userResponse: Option[UserResponse])