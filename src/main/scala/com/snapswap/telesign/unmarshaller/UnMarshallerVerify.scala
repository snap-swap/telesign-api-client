package com.snapswap.telesign.unmarshaller

trait UnMarshallerVerify
  extends CommonUnMarshaller
    with VerifyResponseUnMarshaller
    with PhoneIdResponseUnMarshaller

object UnMarshallerVerify extends UnMarshallerVerify