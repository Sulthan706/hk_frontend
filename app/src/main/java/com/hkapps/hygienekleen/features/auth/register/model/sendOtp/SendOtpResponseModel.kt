package com.hkapps.hygienekleen.features.auth.register.model.sendOtp

data class SendOtpResponseModel(
    val code: Int,
    val status: String,
    val errorCode: String,
    val message: String
)
