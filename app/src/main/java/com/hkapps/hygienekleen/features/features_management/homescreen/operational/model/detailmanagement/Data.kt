package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailmanagement

data class Data(
    val adminMasterId: Int,
    val adminMasterName: String,
    val adminMasterRole: String,
    val levelPosition: Int,
    val levelJabatan: String,
    val adminMasterJabatan: String,
    val adminMasterNUC: String,
    val adminMasterPhone: List<AdminMasterPhone>,
    val adminMasterImage: String,
    val adminMasterEmail: String,
    val idCabang: Int,
    val adminMasterIsActive: Int
)