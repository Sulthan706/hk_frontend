package com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement

data class Content(
    val approveByEmployeeId: Int,
    val createdAt: String,
    val description: String,
    val employeeCode: String,
    val employeeId: Int,
    val employeeName: String,
    val employeePhotoProfile: String,
    val endAt: String,
    val endDate: String,
    val idDetailEmployeeProject: Int,
    val image: String,
    val locationName: String,
    val permissionEmployeeId: Int,
    val plottingId: Int,
    val projectId: String,
    val shiftDescription: String,
    val shiftId: Int,
    val startAt: String,
    val startDate: String,
    val statusPermission: String,
    val subLocationName: String,
    val title: String
)