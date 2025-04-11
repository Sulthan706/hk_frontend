package com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamscheduleteam

data class Data(
    val date: String,
    val listOperator: List<Operator>,
    val listPengawas: List<Pengawas>,
    val projectCode: String,
    val shiftEndAt: String,
    val shiftId: Int,
    val shiftStartAt: String
)