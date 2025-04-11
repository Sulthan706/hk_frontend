package com.hkapps.hygienekleen.features.auth.forgotpass.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPassChangePassRequestModel(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("confirmPassword")
    @Expose
    val confirmPassword: String
)