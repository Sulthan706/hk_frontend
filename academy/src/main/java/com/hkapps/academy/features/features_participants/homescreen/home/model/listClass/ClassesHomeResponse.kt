package com.hkapps.academy.features.features_participants.homescreen.home.model.listClass

data class ClassesHomeResponse(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)