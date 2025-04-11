package com.hkapps.hygienekleen.features.auth.forgotpass.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPassChangePassResponseModel (
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("errorCode")
    @Expose
    val errorCode: String,
    @SerializedName("message")
    @Expose
    val message: String
)