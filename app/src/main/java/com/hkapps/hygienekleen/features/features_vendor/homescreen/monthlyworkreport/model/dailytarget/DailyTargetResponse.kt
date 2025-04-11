package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget

data class DailyTargetResponse(
    val code : Int,
    val status : String,
    val data : DailyTarget
)

data class ListDailyTargetResponse(
    val code : Int,
    val status : String,
    val data : List<DailyTarget>
)
