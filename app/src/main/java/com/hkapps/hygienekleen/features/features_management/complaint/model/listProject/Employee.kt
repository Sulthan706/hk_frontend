package com.hkapps.hygienekleen.features.features_management.complaint.model.listProject

data class Employee(
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
    val levelPosition: Int,
    val levelJabatan: String
)