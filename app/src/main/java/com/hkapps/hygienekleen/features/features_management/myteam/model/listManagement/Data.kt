package com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement

data class Data(
    val adminMasterEmail: String,
    val adminMasterId: Int,
    val adminMasterImage: String,
    val adminMasterIsActive: Int,
    val adminMasterJabatan: String,
    val adminMasterNUC: String,
    val adminMasterName: String,
    val adminMasterPhone: String,
    val adminMasterRole: String,
    val idCabang: Int,
    val levelJabatan: String,
    val levelPosition: Int
)