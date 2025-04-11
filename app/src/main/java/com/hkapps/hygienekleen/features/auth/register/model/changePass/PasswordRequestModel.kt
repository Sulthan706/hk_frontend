package com.hkapps.hygienekleen.features.auth.register.model.changePass

data class PasswordRequestModel(
    val email: String,
    val nuc: String,
    val password: String,
    val confirmPassword: String
)
