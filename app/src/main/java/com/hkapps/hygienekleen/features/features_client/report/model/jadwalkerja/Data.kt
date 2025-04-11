package com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja

data class Data(
    val date: String,
    val listOperator: List<Operator>,
    val listPekerjaan: List<Pekerjaan>,
    val listPengawas: List<Pengawas>,
    val shiftEndAt: String,
    val shiftStartAt: String
)