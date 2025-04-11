package com.hkapps.hygienekleen.features.features_client.report.model.detailPlotting

data class Data(
    val atDate: String,
    val checklistId: Int,
    val checklistReviewId: Int,
    val checklistReviewName: String,
    val createdAt: String,
    val image: String,
    val notes: String,
    val pengawasId: Int,
    val plottingId: Int,
    val projectId: String,
    val shiftId: Int,
    val submitBy: Int,
    val submitByName: String
)