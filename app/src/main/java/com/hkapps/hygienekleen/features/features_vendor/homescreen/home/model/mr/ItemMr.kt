package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

data class ItemMr(
    val idMaterialRequest : Int,
    val rowNumber : Int,
    val itemCode: String,
    val itemName: String,
    val quantity: Int,
    val unitCode: String,
    val used: Int?,
    val reminder: Int?
)
