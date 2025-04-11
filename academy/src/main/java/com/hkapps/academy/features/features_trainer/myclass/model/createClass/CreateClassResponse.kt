package com.hkapps.academy.features.features_trainer.myclass.model.createClass

data class CreateClassResponse(
    val code: Int,
    val `data`: Data,
    val errorCode: String,
    val message: String,
    val status: String
)