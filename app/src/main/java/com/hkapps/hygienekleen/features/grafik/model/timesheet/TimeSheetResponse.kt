package com.hkapps.hygienekleen.features.grafik.model.timesheet

data class TimeSheetResponse(
    val code : Int,
    val data : TimeSheet,
    val status : String,
)

data class ListTimeSheetResponse(
    val code : Int,
    val data : List<ListTimeSheet>,
    val status : String,
)
