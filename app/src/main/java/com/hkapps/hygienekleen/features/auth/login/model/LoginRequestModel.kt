package com.hkapps.hygienekleen.features.auth.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String
)