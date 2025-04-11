package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus

data class Data(
    val codePlottingArea: String,
    val date: String,
    val dayDate: String,
    val doneAt: Any,
    val employeeName: String,
    val employeePhotoProfile: String,
    val endAt: String,
    val idDetailEmployeeProject: Int,
    val idEmployee: Int,
    val idPlotting: Int,
    val idProject: String,
    val idShift: Int,
    val isDone: String,
    val locationId: Int,
    val locationName: String,
    val scheduleType: String,
    val shiftDescription: String,
    val startAt: String,
    val subLocationId: Int,
    val subLocationName: String
)