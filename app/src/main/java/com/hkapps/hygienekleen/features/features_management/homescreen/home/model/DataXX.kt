package com.hkapps.hygienekleen.features.features_management.homescreen.home.model


import com.google.gson.annotations.SerializedName

data class DataXX(
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
    val adminMasterPhone: String,
    @SerializedName("adminMasterPhone2")
    val adminMasterPhone2: String,
    @SerializedName("adminMasterRole")
    val adminMasterRole: String,
    @SerializedName("idCabang")
    val idCabang: Int,
    @SerializedName("levelJabatan")
    val levelJabatan: String,
    @SerializedName("levelPosition")
    val levelPosition: Int,
    @SerializedName("adminMasterReligion")
    val adminMasterReligion: String,
    @SerializedName("adminMasterAddressKtp")
    val adminMasterAddressKtp: String

)