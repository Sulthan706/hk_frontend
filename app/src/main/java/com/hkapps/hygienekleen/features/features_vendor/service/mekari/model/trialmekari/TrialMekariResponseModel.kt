package com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.trialmekari

data class TrialMekariResponseModel(
    val code: Int,
    val errorCode: String,
    val errors: Any,
    val message: String,
    val status: String
)