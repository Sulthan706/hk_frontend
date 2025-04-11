package com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.createOvertimeChange

data class Data(
    val overtimeId: Int,
    val employeeId: Int,
    val employeeJobCode: String,
    val employeeReplaceId: Int,
    val employeeReplaceJobCode: String,
    val createdByEmployeeId: Int,
    val idDetailEmployeeProject: Int,
    val projectId: String,
    val title: String,
    val description: String,
    val plottingId: Int,
    val shiftId: Int,
    val atDate: String,
    val image: String,
    val status: String,
    val processAt: String,
    val permissionId: Int,
    val isPermission: String,
    val createdAt: String,
    val updatedAt: String
)