package com.hkapps.hygienekleen.features.features_client.report.model.listshiftreport

data class ListShiftReportResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)