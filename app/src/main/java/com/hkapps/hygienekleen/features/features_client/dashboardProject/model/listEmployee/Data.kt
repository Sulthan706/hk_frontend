package com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee

data class Data(
    val date: String,
    val listPekerjaan: List<ListPekerjaan>,
    val listOperator: List<Operator>,
    val listPengawas: List<Pengawas>,
    val shiftEndAt: String,
    val shiftStartAt: String
)