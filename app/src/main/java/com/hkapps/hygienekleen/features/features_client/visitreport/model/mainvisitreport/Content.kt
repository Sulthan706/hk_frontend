package com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport

data class Content(
    val adminMasterId: Int,
    val adminMasterImage: String,
    val adminMasterJabatan: String,
    val adminMasterName: String,
    val adminMasterPhone: String,
    val checkIn: String,
    val checkOut: String
)