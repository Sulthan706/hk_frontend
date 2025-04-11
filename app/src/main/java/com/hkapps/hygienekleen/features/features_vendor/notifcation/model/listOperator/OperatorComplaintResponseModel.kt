package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.listOperator

data class OperatorComplaintResponseModel(
    val code: Int,
    val status: String,
    val data: List<DataOperator>
)
