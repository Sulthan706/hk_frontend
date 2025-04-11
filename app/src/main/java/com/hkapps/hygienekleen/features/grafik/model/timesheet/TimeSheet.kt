package com.hkapps.hygienekleen.features.grafik.model.timesheet

data class TimeSheet(
    val totalSchedule: Int,
    val totalScheduleOvertime: Int,
    val offSchedule: Int,
    val notAbcentSchedule: Int,
    val onDutySchedule: Int,
    val abcentSchedule: Int,
    val alfaSchedule: Int,
    val abcentScheduleOvertime: Int,
    val alfaScheduleOvertime: Int,
    val sickAndIzinSchedule: Int,
    val notAbcentInPercent: Double,
    val onDutyInPercent: Double,
    val abcentInPercent: Double,
    val alfaInPercent: Double,
    val sickAndIzinInPercent: Double,
    val abcentOvertimeInPercent: String,
    val alfaOvertimeInPercent: String

){
    fun getAbcentOvertimeInDouble(): Double? {
        return abcentOvertimeInPercent?.toDoubleOrNull()
    }

    fun getAlfaOvertimeInDouble(): Double? {
        return alfaOvertimeInPercent?.toDoubleOrNull()
    }
}
