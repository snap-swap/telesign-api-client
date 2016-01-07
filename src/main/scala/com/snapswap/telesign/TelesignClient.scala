package com.snapswap.telesign

import com.snapswap.telesign.model.{VerifyResponse, EnumUseCaseCodes}

import scala.concurrent.Future

trait TelesignClient {
  def phoneIdScore(number: String, useCaseCode: EnumUseCaseCodes.UseCaseCode): Future[PhoneIdScoreResult]

  def smsVerify(number: String, language: String = "en-US", template: String = "Your code is $$CODE$$"): Future[String]

  def verifyInfo(referenceId: String): Future[VerifyResponse]
}