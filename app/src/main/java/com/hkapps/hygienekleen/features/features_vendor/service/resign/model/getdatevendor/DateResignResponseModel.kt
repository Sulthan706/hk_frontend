package com.hkapps.hygienekleen.features.features_vendor.service.resign.model.getdatevendor

data class DateResignResponseModel(
    val code: Int,
    val errorCode: String,
    val message: String,
    val status: String,
    val data: String
)