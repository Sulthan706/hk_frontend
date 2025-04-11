package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.profileManagement


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("adminMasterId")
    val adminMasterId: Int,
    @SerializedName("adminMasterName")
    val adminMasterName: String,
    @SerializedName("adminMasterRole")
    val adminMasterRole: String,
    @SerializedName("levelPosition")
    val levelPosition: Int,
    @SerializedName("levelJabatan")
    val levelJabatan: String,
    @SerializedName("adminMasterJabatan")
    val adminMasterJabatan: String,
    @SerializedName("adminMasterNUC")
    val adminMasterNUC: String,
    @SerializedName("adminMasterPhone")
    val adminMasterPhone: List<AdminMasterPhone>,
    @SerializedName("adminMasterImage")
    val adminMasterImage: String,
    @SerializedName("adminMasterEmail")
    val adminMasterEmail: String,
    @SerializedName("adminMasterAddress")
    val adminMasterAddress: String,
    @SerializedName("adminMasterBirthDate")
    val adminMasterBirthDate: String,
    @SerializedName("adminMasterPlaceOfBirth")
    val adminMasterPlaceOfBirth: String,
    @SerializedName("adminMasterGender")
    val adminMasterGender: String,
    @SerializedName("adminMasterMarriageStatus")
    val adminMasterMarriageStatus: String,
    @SerializedName("adminMasterMother")
    val adminMasterMother: String,
    @SerializedName("idCabang")
    val idCabang: Int,
    @SerializedName("adminMasterIsActive")
    val adminMasterIsActive: Int,
)