package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceInOutBod

data class Data(
    val address: String,
    val dateRkbBod: String,
    val employeeImgSelfieKeluar: Any,
    val employeeImgSelfieMasuk: String,
    val idRkbBod: Int,
    val latitude: String,
    val longitude: String,
    val scanKeluar: Any,
    val scanMasuk: String,
    val statusRkbBod: String,
    val tipeRkbBod: Any
)