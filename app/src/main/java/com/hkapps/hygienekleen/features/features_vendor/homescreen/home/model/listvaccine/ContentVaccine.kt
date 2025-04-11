package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine

data class ContentVaccine(
    val employeeId: Int,
    val idVaccine: Int,
    val uploadDate: String,
    val vaccineCertificate: String,
    val vaccineType: String
)