package com.hkapps.hygienekleen.features.grafik.model.timesheet

data class ListTimeSheet(
    val totalSchedule : Int,
    val offSchedule : Int,
    val notAbcentSchedule : Int,
    val onDutySchedule : Int,
    val abcentSchedule : Int,
    val date : String,
    val notAbcentInPercent : Double,
    val onDutyInPercent : Double,
    val abcentInPercent : Double
)
