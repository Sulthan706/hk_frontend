package com.hkapps.hygienekleen.features.features_client.myteam.model

data class DataManagementStructuralClientModel(
    val adminMasterId: Int,
    val adminMasterName: String,
    val adminMasterRole: String,
    val levelPosition: Int,
    val levelJabatan: String,
    val adminMasterJabatan: String,
    val adminMasterNUC: String,
    val adminMasterPhone: String,
    val adminMasterImage: String,
    val adminMasterEmail: String,
    val idCabang: Int,
    val adminMasterIsActive: Int
)