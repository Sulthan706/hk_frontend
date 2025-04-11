package com.hkapps.hygienekleen.features.features_management.homescreen.closing.model

data class DailyTargetManagementResponse(
   val code : Int,
   val status : String,
   val data : ListDailyTargetClosing
)

data class DailyDetailTargetManagementResponse(
   val code : Int,
   val status : String,
   val data : DailyTargetManagement
)
