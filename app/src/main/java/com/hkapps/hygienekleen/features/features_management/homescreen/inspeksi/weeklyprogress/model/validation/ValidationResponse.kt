package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.validation

data class ValidationResponse(
    val code : Int,
    val status : String,
    val data : Validation,
    val errorCode : String?,
    val message : String?,
)
