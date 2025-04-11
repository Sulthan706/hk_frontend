package com.hkapps.hygienekleen.features.features_management.homescreen.closing.model

data class DailyTargetManagement(
    val date: String,
    val name: String?,
    val shift: String?,
    val totalTarget: Int,
    val targetsDone: Int,
    val targetsNotFulfilled: Int,
    val percentTargetsDone: Double,
    val percentTargetsNotFulfilled: Double,
    val totalDiverted: Int,
    val idDetailEmployeeProject: Int,
    val scheduleType: String?,
    val closingStatus: String,
    val file : String?,
    val closedBy : String?,
    val closedAt : String?,
    val fileGenerated : Boolean,
    val emailSent : Boolean,
    val userRole: String,
    val targetsRealization: Int,
    val percentTargetsRealization: Double,
    val listRealizationItem: String,
    val targetsNotApproved: Int,
    val percentTargetsNotApproved: Double,
    val teamLeadTotalClosing: Int,
    val teamLeadDoneClosing: Int,
    val supervisorTotalClosing: Int,
    val supervisorDoneClosing: Int,
    val projectName: String,
    val projectCode: String,
    val branchName: String,
    val scanOutHasPassed: Boolean = false,
    val idClosing : Int?,
)

data class ListDailyTargetClosing(
    val projectName : String,
    val projectCode : String,
    val branchName : String,
    val listTarget : List<DailyTargetManagement>
)
