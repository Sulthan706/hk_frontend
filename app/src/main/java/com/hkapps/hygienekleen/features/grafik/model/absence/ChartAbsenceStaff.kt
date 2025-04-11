package com.hkapps.hygienekleen.features.grafik.model.absence

data class ChartAbsenceStaff(
    val totalSchedule : Int,
    val offSchedule : Int,
    val notAbcentSchedule : Int,
    val onDutySchedule : Int,
    val abcentSchedule : Int,
    val notAbcentInPercent : Double,
    val onDutyInPercent : Double,
    val abcentInPercent : Double

)
