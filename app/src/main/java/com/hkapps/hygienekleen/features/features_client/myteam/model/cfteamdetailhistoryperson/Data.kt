package com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetailhistoryperson

data class Data(
    val idProject: String,
    val scheduleAlpha: Int,
    val scheduleHadir: Int,
    val scheduleIzin: Int,
    val scheduleLemburGanti: Int,
    val shiftAkhir: String,
    val shiftMulai: String,
    val statusSchedule: String,
    val tglSchedule: String,
    val tipeJadwal: List<TipeJadwal>
)