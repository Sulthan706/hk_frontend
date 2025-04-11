package com.hkapps.hygienekleen.features.features_vendor.profile.model


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("employeeAddress")
    val employeeAddress: String,
    @SerializedName("employeeBirthDate")
    val employeeBirthDate: String,
    @SerializedName("employeeEmail")
    val employeeEmail: String,
    @SerializedName("employeeGender")
    val employeeGender: String,
    @SerializedName("employeeJobCode")
    val employeeJobCode: String,
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
    val employeePhoneNumber: String,
    @SerializedName("employeePhoneNumber2")
    val employeePhoneNumber2: String,
    @SerializedName("employeePhotoProfile")
    val employeePhotoProfile: String,
    @SerializedName("employeePlaceOfBirth")
    val employeePlaceOfBirth: String,
    @SerializedName("idEmployee")
    val idEmployee: Int,
    @SerializedName("employeeKtpAddress")
    val employeeKtpAddress: Int
)