package com.snapswap.telesign

import scala.concurrent.Future

trait TelesignClient {
  def getScore(number: String): Future[PhoneScore]

  def initiateVerification(number: String): Future[PhoneVerificationId]

  def getVerification(id: PhoneVerificationId): Future[PhoneVerification]
}