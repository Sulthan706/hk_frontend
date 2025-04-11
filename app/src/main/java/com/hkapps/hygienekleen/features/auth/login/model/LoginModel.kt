package com.hkapps.hygienekleen.features.auth.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("errorCode")
    @Expose
    val errorCode: String,
    @SerializedName("message")
    @Expose
    val message: String
)