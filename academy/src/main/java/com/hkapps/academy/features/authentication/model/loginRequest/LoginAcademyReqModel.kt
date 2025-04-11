package com.hkapps.academy.features.authentication.model.loginRequest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginAcademyReqModel(
    @SerializedName("userNuc")
    @Expose
    val userNuc: String

//    @SerializedName("email")
//    @Expose
//    val email: String,
//
//    @SerializedName("password")
//    @Expose
//    val password: String
)
