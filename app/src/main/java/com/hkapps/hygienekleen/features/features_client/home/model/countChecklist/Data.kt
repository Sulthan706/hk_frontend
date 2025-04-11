package com.hkapps.hygienekleen.features.features_client.home.model.countChecklist

data class Data(
    val date: String,
    val projectCode: String,
    val totalPlotting: Int,
    val totalPlottingChecklist: Int
)