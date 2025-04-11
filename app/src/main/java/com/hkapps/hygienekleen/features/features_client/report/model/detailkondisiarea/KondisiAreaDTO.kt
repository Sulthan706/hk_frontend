package com.hkapps.hygienekleen.features.features_client.report.model.detailkondisiarea

data class KondisiAreaDTO(
    val checkDate: String,
    val checkPengawasId: Int,
    val checkPengawasName: String,
    val description: String,
    val imageChecklist: String,
    val statusCondition: String
)