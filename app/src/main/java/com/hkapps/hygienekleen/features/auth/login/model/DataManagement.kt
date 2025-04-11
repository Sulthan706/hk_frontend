package com.hkapps.hygienekleen.features.auth.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataManagement(
    @SerializedName("adminMasterId")
    @Expose
    val adminMasterId: Int,
    @SerializedName("adminMasterName")
    @Expose
    val adminMasterName: String,
    @SerializedName("adminMasterRole")
    @Expose
    val adminMasterRole: String,
    @SerializedName("levelJabatan")
    @Expose
    val levelJabatan: String,
    @SerializedName("adminMasterJabatan")
    @Expose
    val adminMasterJabatan: String,
    @SerializedName("adminMasterNUC")
    @Expose
    val adminMasterNUC: String,
    @SerializedName("adminMasterPhone")
    @Expose
    val adminMasterPhone: String,
    @SerializedName("idCabang")
    @Expose
    val idCabang: Int,
    @SerializedName("branchCode")
    @Expose
    val branchCode: String,
    @SerializedName("branchName")
    @Expose
    val branchName: String,
    @SerializedName("totalProject")
    @Expose
    val totalProject: Int,
    @SerializedName("adminMasterEmail")
    @Expose
    val adminMasterEmail: String,
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("refreshToken")
    @Expose
    val refreshToken: String,
    @SerializedName("levelPosition")
    @Expose
    val levelPosition: Int

)