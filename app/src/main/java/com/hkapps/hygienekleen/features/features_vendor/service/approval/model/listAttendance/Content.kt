package com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance

data class Content(
    val address: String,
    val approvedBy: Int,
    val createdAt: String,
    val description: String,
    val employeeCode: String,
    val employeeId: Int,
    val employeeImages: String,
    val employeeName: String,
    val idSchedule: Int,
    val idUserFlying: Int,
    val jobCode: String,
    val latitude: String,
    val longitude: String,
    val projectCode: String,
    val shift: String,
    val shiftEndAt: String,
    val shiftStartAt: String,
    val status: String,
    val statusAttendance: String,
    val typeReport: String,
    val updatedAt: String
)