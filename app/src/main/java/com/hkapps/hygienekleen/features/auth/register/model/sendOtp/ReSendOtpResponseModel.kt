package com.hkapps.hygienekleen.features.auth.register.model.sendOtp

class ReSendOtpResponseModel(
    val code: Int,
    val status: String,
    val errorCode: String,
    val message: String
)