package com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja

data class Pekerjaan(
    val activity: String,
    val endAt: String,
    val idSubLocationActivity: Int,
    val shiftDescription: String,
    val startAt: String
)