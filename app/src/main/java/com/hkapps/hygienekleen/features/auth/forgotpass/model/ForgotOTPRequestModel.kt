package com.hkapps.hygienekleen.features.auth.forgotpass.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotOTPRequestModel (
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("codeOtp")
    @Expose
    val codeOtp: String
)