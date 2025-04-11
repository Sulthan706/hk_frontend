package com.hkapps.academy.features.features_participants.classes.model.listClass

data class ClassesParticipantResponse(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)