package com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectEmployee

data class Data(
    val idEmployee: Int,
    val idEmployeeProject: Int,
    val employeeNuc: String,
    val employeeName: String,
    val employeePhoneNumber: String,
    val employeeEmail: String,
    val employeeNik: String,
    val employeeJobCode: String,
    val employeePhotoProfile: String,
    val employeeAddress: String,
    val employeeBirthDate: String,
    val employeePlaceOfBirth: String,
    val employeeGender: String,
    val employeeMarriageStatus: String,
    val employeeMotherName: String,
    val shiftDescription: String,
    val shiftId: Int
)

