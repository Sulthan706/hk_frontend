package com.hkapps.hygienekleen.features.features_vendor.profile.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("employeeAddress")
    val employeeAddress: String,
    @SerializedName("employeeBirthDate")
    val employeeBirthDate: String,
    @SerializedName("employeeEmail")
    val employeeEmail: String,
    @SerializedName("employeeGender")
    val employeeGender: String,
    @SerializedName("employeeMarriageStatus")
    val employeeMarriageStatus: String,
    @SerializedName("employeeMotherName")
    val employeeMotherName: String,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("employeeNik")
    val employeeNik: String,
    @SerializedName("employeeNuc")
    val employeeNuc: String,
    @SerializedName("employeePhoneNumber")
    val employeePhoneNumber: List<EmployeePhoneNumber>,
    @SerializedName("employeePhotoProfile")
    val employeePhotoProfile: String,
    @SerializedName("employeePlaceOfBirth")
    val employeePlaceOfBirth: String,
    @SerializedName("idEmployee")
    val idEmployee: Int,
    @SerializedName("job")
    val job: Any,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("jobLevel")
    val jobLevel: String,
    @SerializedName("jobName")
    val jobName: String,
    @SerializedName("project")
    val project: Project,
    @SerializedName("countVaccine")
    val countVaccine: Int,
    @SerializedName("countDoc")
    val countDoc: Int,
    @SerializedName("checkBankingAccount")
    val checkBankingAccount: Boolean,
    @SerializedName("checkProfile")
    val checkProfile: Boolean,
    @SerializedName("checkFamilyCard")
    val checkFamilyCard: Boolean,
    @SerializedName("checkImageKtp")
    val checkImageKtp: Boolean,
    @SerializedName("bankingName")
    val bankingName: String,
    @SerializedName("bankingNumber")
    val bankingNumber: String,
    @SerializedName("familyCardNumber")
    val familyCardNumber: String,
    @SerializedName("employeeBpjsKesehatan")
    val employeeBpjsKesehatan: String,
    @SerializedName("employeeBpjsKetenagakerjaan")
    val employeeBpjsKetenagakerjaan: String,
    @SerializedName("employeeBpjsKesehatanFile")
    val employeeBpjsKesehatanFile: String,
    @SerializedName("employeeBpjsKetenagakerjaanFile")
    val employeeBpjsKetenagakerjaanFile: String,
    @SerializedName("employeeKtpAddress")
    val employeeKtpAddress: String,
    @SerializedName("employeeReligion")
    val employeeReligion: String,
    @SerializedName("employeeCountChildren")
    val employeeCountChildren: String,
)