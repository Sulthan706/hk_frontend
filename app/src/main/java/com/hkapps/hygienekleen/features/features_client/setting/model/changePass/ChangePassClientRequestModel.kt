package com.hkapps.hygienekleen.features.features_client.setting.model.changePass

data class ChangePassClientRequestModel(
    val clientId: Int,
    val oldPassword: String,
    val password: String,
    val confirmPassword: String
)
