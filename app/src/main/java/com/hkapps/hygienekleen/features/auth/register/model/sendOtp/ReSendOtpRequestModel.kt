package com.hkapps.hygienekleen.features.auth.register.model.sendOtp

data class ReSendOtpRequestModel(
    val email: String,
    val nuc: String
)
