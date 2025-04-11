package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.checknews

data class GetCheckNewsResponseModel(
    val code: Int,
    val errorCode: String,
    val message: String,
    val status: String
)