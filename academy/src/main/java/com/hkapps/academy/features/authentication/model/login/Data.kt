package com.hkapps.academy.features.authentication.model.login

data class Data(
    val email: String,
    val jabatan: String,
    val levelJabatan: String,
    val loginAs: String,
    val name: String,
    val refreshToken: String,
    val role: String,
    val token: String,
    val userNuc: String,
    val projectCode: String
)