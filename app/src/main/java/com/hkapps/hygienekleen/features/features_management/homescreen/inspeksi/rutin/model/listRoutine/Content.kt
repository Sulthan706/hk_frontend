package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine

data class Content(
    val idroutine: Int,
    val adminMasterId: Int,
    val adminMasterName: String,
    val projectCode: String,
    val projectName: String,
    val date: String,
    val title: String,
    val description: String,
    val file: String,
    val fileDescription: String
)
