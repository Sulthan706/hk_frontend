package com.hkapps.hygienekleen.features.features_client.report.model.detailkondisiarea

data class Data(
    val date: String,
    val kondisiAreaDTO: KondisiAreaDTO,
    val shiftEndAt: String,
    val shiftStartAt: String
)