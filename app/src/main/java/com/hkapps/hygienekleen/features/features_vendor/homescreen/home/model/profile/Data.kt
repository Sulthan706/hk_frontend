package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile

import com.hkapps.hygienekleen.features.features_vendor.profile.model.EmployeePhoneNumber
import com.hkapps.hygienekleen.features.features_vendor.profile.model.Project
import com.hkapps.hygienekleen.features.features_vendor.profile.model.Job


data class Data(
    val bankingName: String,
    val bankingNumber: String,
    val checkBankingAccount: Boolean,
    val checkFamilyCard: Boolean,
    val checkProfile: Boolean,
    val countDoc: Int,
    val lastUpdatedProfile : String?,
    val countVaccine: Int,
    val employeeAddress: String,
    val employeeBirthDate: String,
    val employeeBpjsKesehatan: String,
    val employeeBpjsKesehatanFile: String,
    val employeeBpjsKetenagakerjaan: String,
    val employeeBpjsKetenagakerjaanFile: String,
    val employeeEmail: String,
    val employeeGender: String,
    val employeeKtpAddress: Any,
    val employeeMarriageStatus: String,
    val employeeMotherName: String,
    val employeeName: String,
    val employeeNik: String,
    val employeeNuc: String,
    val employeePhoneNumber: List<EmployeePhoneNumber>,
    val employeePhotoProfile: String?,
    val employeePlaceOfBirth: String,
    val employeeReligion: Any,
    val familyCardNumber: String,
    val idEmployee: Int,
    val isActive: String,
    val job: Job,
    val jobCode: String,
    val jobLevel: String,
    val levelJabatan : String?,
    val jobName: String,
    val project: Project
)