package com.hkapps.hygienekleen.features.features_client.overtime.model.listSubLoc

data class SubLocOvertimeClientResponse(
    val code: Int,
    val status: String,
    val data: List<Data>
)