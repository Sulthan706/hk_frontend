package com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel

data class CheckCreatePermissionResponse(
    val code: Int,
    val errorCode: String,
    val message: String,
    val status: String
)