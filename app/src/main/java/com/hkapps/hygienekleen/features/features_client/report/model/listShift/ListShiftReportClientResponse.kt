package com.hkapps.hygienekleen.features.features_client.report.model.listShift

data class ListShiftReportClientResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)