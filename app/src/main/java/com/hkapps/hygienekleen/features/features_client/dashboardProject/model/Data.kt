package com.hkapps.hygienekleen.features.features_client.dashboardProject.model

data class Data(
    val countArea: Int,
    val countBelumAbsen: Int,
    val countCfManagement: Int,
    val countLibur: Int,
    val countManpower: Int,
    val countSedangBekerja: Int,
    val countSelesaiBekerja: Int,
    val listAreaProject: List<AreaProject>,
    val projectCode: String,
    val projectName: String
)