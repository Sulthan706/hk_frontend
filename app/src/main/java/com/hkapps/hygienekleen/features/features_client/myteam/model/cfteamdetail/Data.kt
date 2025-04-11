package com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetail

data class Data(
    val employeeCode: String,
    val employeeId: Int,
    val employeeJob: String,
    val employeeName: String,
    val employeePhone1: String,
    val employeePhone2: String,
    val scheduleAlpha: Int,
    val scheduleHadir: Int,
    val scheduleIzin: Int,
    val scheduleLemburGanti: Int,
    val scheduleOff: Int,
    val totalPercentage: Double
)