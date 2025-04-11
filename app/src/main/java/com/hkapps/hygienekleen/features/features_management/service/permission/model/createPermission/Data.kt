package com.hkapps.hygienekleen.features.features_management.service.permission.model.createPermission

data class Data(
    val approveByEmployeeId: Int,
    val createdAt: String,
    val description: String,
    val endDate: String,
    val idDetailEmployeeProject: Int,
    val image: String,
    val permissionEmployeeId: Int,
    val plottingId: Int,
    val processAt: String,
    val projectId: String,
    val requestByEmployeeId: Int,
    val shiftId: Int,
    val startDate: String,
    val statusPermission: String,
    val title: String,
    val updatedAt: String
)