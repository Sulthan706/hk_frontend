package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.detailRoutine

data class Data(
    val adminMasterId: Int,
    val adminMasterName: String,
    val date: String,
    val description: String,
    val `file`: String,
    val fileDescription: String,
    val idroutine: Int,
    val projectCode: String,
    val projectName: String,
    val title: String
)