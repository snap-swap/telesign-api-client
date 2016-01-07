package com.snapswap.telesign.model

/**
  * An object that describes the risk score for the phone number specified in the request.
  *
  * @param level          A string indicating the severity of the risk.
  * @param score          One of the <strong>Score</strong> values.
  * @param recommendation A string indicating the action that TeleSign recommends that you take based on the risk score.
  */
case class Risk(level: String,
                score: Int,
                recommendation: String)