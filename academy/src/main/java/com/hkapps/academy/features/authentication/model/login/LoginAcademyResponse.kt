package com.hkapps.academy.features.authentication.model.login

data class LoginAcademyResponse(
    val code: Int,
    val `data`: Data,
    val status: String
)