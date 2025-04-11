package com.hkapps.hygienekleen.features.auth.forgotpass.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotRequestModel(
    @SerializedName("email")
    @Expose
    val email: String
)