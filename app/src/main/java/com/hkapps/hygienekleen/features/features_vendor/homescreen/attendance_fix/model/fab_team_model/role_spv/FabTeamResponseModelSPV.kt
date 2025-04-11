package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.role_spv


import com.google.gson.annotations.SerializedName

data class FabTeamResponseModelSPV(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)