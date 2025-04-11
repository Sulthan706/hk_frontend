package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailoperational

data class Data(
    val employeeEmail: String,
    val employeeName: String,
    val employeeNik: String,
    val employeeNuc: String,
    val employeePhoneNumber: List<EmployeePhone>,
    val employeePhotoProfile: String,
    val idEmployee: Int,
    val job: Job,
    val jobCode: String,
    val jobLevel: String,
    val jobName: String,
    val project: Project
)