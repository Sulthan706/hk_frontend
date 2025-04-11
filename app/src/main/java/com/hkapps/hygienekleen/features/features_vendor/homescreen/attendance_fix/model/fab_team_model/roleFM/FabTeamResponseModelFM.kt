package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.roleFM


import com.google.gson.annotations.SerializedName

data class FabTeamResponseModelFM(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)