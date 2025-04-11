package com.hkapps.hygienekleen.features.features_management.service.overtime.model.listOvertimeChange

data class Content(
    val date: String,
    val dateParameter: String,
    val employeeId: Int,
    val employeeJobLevel: String,
    val employeeName: String,
    val employeePhotoProfile: String,
    val employeeReplaceById: Int,
    val employeeReplaceByName: String,
    val areaName: String,
    val subAreaName: String,
    val projectCode: String,
    val projectName: String,
    val shiftDescription: String,
    val shiftId: Int,
    val statusAttendance: String
)