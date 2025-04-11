package com.hkapps.hygienekleen.features.facerecog.model.regisfacerecog

data class RegisFaceResponseModel(
    val code: Int,
    val errorCode: String,
    val message: String,
    val status: String
)