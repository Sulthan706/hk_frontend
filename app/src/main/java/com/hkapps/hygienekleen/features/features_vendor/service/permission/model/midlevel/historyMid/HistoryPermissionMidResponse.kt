package com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.historyMid

import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.ListHistoryPermission

data class HistoryPermissionMidResponse(
    val code: Int,
    val `data`: List<ListHistoryPermission>,
    val status: String,
    val errorCode: String,
    val message: String
)