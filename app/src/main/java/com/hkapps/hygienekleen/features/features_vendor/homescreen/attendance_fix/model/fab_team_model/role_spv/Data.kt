package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.role_spv


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("alphaCount")
    val alphaCount: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("hadirCount")
    val hadirCount: Int,
    @SerializedName("izinCount")
    val izinCount: Int,
    @SerializedName("listEmployee")
    val listEmployee: List<Employee>,
    @SerializedName("lupaAbsenCount")
    val lupaAbsenCount: Int,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("shiftId")
    val shiftId: Int
)