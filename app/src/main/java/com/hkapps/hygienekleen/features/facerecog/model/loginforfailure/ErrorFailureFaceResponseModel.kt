package com.hkapps.hygienekleen.features.facerecog.model.loginforfailure

data class ErrorFailureFaceResponseModel(
    val code: Int,
    val message: String,
    val status: String
)