package com.hkapps.hygienekleen.features.auth.register.model.verifyOtp

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequestModel(
    val email: String,
    @SerializedName("codeOtp")
    val otp: String
)
