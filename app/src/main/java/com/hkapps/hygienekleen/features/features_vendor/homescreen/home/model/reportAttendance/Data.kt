package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.reportAttendance

data class Data(
    val alpaCount: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeePhotoProfile: String,
    val hadirCount: Int,
    val izinCount: Int,
    val jobCode: String,
    val jobName: String,
    val lemburGantiCount: Int,
    val levelJabatan: String,
    val persentaseKehadiran: Double,
    val projectId: String,
    val projectName: String,
    val terlambatCount: Int,
    val totalHari: Int,
    val totalJamKerja: Int,
    val startingFrom: String,
    val jobRole: String,
    val isSeePayroll: String,
    val previousMonth: String
)