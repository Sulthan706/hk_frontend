package com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.createOvertimeResign

data class Data(
    val atDate: String,
    val createdAt: String,
    val createdByEmployeeId: Int,
    val description: String,
    val employeeId: Int,
    val employeeJobCode: String,
    val employeeReplaceId: Int,
    val employeeReplaceJobCode: String,
    val idDetailEmployeeProject: Int,
    val image: Any,
    val isPermission: String,
    val overtimeId: Int,
    val permissionId: Any,
    val plottingId: Int,
    val processAt: String,
    val projectId: String,
    val shiftId: Int,
    val status: String,
    val title: String,
    val updatedAt: Any
)