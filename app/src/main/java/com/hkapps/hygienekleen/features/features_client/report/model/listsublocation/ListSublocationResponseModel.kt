package com.hkapps.hygienekleen.features.features_client.report.model.listsublocation

data class ListSublocationResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)