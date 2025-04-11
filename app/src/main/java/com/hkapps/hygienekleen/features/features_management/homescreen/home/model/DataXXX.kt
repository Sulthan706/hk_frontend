package com.hkapps.hygienekleen.features.features_management.homescreen.home.model


import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.profileManagement.AdminMasterPhone
import com.google.gson.annotations.SerializedName

data class DataXXX(
    @SerializedName("adminMasterEmail")
    val adminMasterEmail: String,
    @SerializedName("adminMasterId")
    val adminMasterId: Int,
    @SerializedName("adminMasterImage")
    val adminMasterImage: String,
    @SerializedName("adminMasterIsActive")
    val adminMasterIsActive: Int,
    @SerializedName("adminMasterJabatan")
    val adminMasterJabatan: String,
    @SerializedName("adminMasterNUC")
    val adminMasterNUC: String,
    @SerializedName("adminMasterName")
    val adminMasterName: String,
    @SerializedName("adminMasterPhone")
    val adminMasterPhone: List<AdminMasterPhone>,
    @SerializedName("adminMasterRole")
    val adminMasterRole: String,
    @SerializedName("idCabang")
    val idCabang: Int,
    @SerializedName("levelJabatan")
    val levelJabatan: String,
    @SerializedName("levelPosition")
    val levelPosition: Int
)