package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model

import com.google.gson.annotations.SerializedName

class QRCodeData(
    @SerializedName("idBarcode")
    val idBarcode: Int,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("barcodeKey")
    val barcodeKey: String,
    @SerializedName("employeePhoneNumber")
    val employeePhoneNumber: String,
    @SerializedName("checkIn")
    val checkIn: String,
    @SerializedName("checkOut")
    val checkOut: String
)