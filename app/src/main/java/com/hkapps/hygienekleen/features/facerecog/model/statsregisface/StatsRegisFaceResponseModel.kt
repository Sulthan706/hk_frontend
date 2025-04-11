package com.hkapps.hygienekleen.features.facerecog.model.statsregisface

data class StatsRegisFaceResponseModel(
    val code: Int,
    val errorCode: String?,
    val message: String,
    val status: String
)