package com.hkapps.hygienekleen.features.features_client.report.model.listlocation

data class ListLocationResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)