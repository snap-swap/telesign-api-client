package com.snapswap.telesign.model

/**
  * An object containing details about the request status.
  *
  * @param updatedOn   An ISO 8601 UTC timestamp indicating when the transaction status was updated.
  * @param code        One of the<emphasis> Transaction Status Codes</emphasis>.
  * @param description A description of the transaction status.
  */
case class Status(updatedOn: String,
                  code: Int,
                  description: String)