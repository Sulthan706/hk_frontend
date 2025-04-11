package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod

data class Data(
    val listEmployeeDetail: ListEmployeeDetail,
    val percentageChiefSupervisor: Double,
    val percentageGondola: Double,
    val percentageOperator: Double,
    val percentageSupervisor: Double,
    val percentageTeamLeader: Double,
    val totalChiefSupervisor: Int,
    val totalEmployeeAktif: Int,
    val totalGondola: Int,
    val totalNew: Int,
    val totalOperator: Int,
    val totalResign: Int,
    val totalSupervisor: Int,
    val totalTeamLeader: Int
)