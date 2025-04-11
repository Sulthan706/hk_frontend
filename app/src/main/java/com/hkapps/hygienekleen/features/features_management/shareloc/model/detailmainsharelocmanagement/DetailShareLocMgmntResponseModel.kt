package com.hkapps.hygienekleen.features.features_management.shareloc.model.detailmainsharelocmanagement

data class DetailShareLocMgmntResponseModel(
    val code: Int,
    val `data`: Data,
    val status: String,
    val message: String,
    val errorCode: String
)