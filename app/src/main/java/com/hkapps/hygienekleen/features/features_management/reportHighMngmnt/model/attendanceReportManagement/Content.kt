package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement

data class Content(
    val rowNumber: Int,
    val alpaCount: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeeCode: String,
    val employeePhotoProfile: String,
    val hadirCount: Int,
    val isSeePayroll: Any,
    val izinCount: Int,
    val jobCode: String,
    val jobName: String,
    val jobRole: Any,
    val lemburGantiCount: Int,
    val levelJabatan: Any,
    val persentaseKehadiran: Double,
    val persentaseKetidakhadiran: Double,
    val persentaseTerlambat: Double,
    val previousMonth: Any,
    val previousMonthInt: Int,
    val projectId: String,
    val projectName: String,
    val startingFrom: Any,
    val terlambatCount: Int,
    val totalHari: Int,
    val totalJamKerja: Int
)