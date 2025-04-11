package com.hkapps.hygienekleen.features.facerecog.model.facerecognew

data class ListRandomGestureResponse(
    val success : String,
    val code : Int,
    val message : String,
    val errorCode : String,
    val data : List<String>
)
