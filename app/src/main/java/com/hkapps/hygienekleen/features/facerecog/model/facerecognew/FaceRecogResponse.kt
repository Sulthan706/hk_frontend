package com.hkapps.hygienekleen.features.facerecog.model.facerecognew

data class FaceRecogResponse<T>(
    val status : String,
    val code : Int,
    val errorCode : String?,
    val message : String,
    val data : T?
)
