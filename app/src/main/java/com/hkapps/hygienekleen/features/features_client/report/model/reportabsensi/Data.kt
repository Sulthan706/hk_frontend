package com.hkapps.hygienekleen.features.features_client.report.model.reportabsensi

data class Data(
    val countHadir: Int,
    val countJadwalLibur: Int,
    val countJadwalMasuk: Int,
    val countLemburGanti: Int,
    val countLemburTagih: Int,
    val countTidakHadir: Int,
    val inPercent: Int,
    val projectCode: String,
    val projectName: String
)