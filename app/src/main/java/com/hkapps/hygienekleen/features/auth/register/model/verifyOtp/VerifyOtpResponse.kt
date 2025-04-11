package com.hkapps.hygienekleen.features.auth.register.model.verifyOtp

data class VerifyOtpResponse(
    val code: Int,
    val status: String,
    val errorCode: String,
    val message: String
)
