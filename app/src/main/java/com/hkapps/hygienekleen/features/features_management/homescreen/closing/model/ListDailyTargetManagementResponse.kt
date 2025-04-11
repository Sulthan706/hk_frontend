package com.hkapps.hygienekleen.features.features_management.homescreen.closing.model

data class ListDailyTargetManagementResponse(
    val code : Int,
    val status : String,
    val data : List<ListDailyTargetClosing>
)
