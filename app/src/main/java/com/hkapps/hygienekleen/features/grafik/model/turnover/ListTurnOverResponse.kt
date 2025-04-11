package com.hkapps.hygienekleen.features.grafik.model.turnover

data class ListTurnOverResponse(
    val code : Int,
    val data : List<ListTurnOver>,
    val status : String,
)
