package com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel

data class OperatorResponseModel(
    val code: Int,
    val status: String,
    val data: List<DataOperator>
)