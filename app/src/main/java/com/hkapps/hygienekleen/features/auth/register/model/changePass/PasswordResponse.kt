package com.hkapps.hygienekleen.features.auth.register.model.changePass

data class PasswordResponse(
    val code: Int,
    val status: String,
    val errorCode: String,
    val message: String
)
