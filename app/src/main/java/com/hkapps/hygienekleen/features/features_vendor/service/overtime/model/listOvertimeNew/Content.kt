package com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.listOvertimeNew

data class Content(
    val date: String,
    val dateParameter: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeJobLevel: String,
    val employeePhotoProfile: String,
    val employeeReplaceById: Int,
    val employeeReplaceByName: String,
    val areaName: String,
    val subAreaName: String,
    val projectCode: String,
    val shiftId: Int,
    val shiftDescription: String,
    val statusAttendance: String
)