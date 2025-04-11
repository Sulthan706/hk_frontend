package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("alphaCount")
    val alphaCount: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("employeeCode")
    val employeeCode: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("hadirCount")
    val hadirCount: Int,
    @SerializedName("izinCount")
    val izinCount: Int,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("lemburGantiCount")
    val lemburGantiCount: Int,
    @SerializedName("liburCount")
    val liburCount: Int,
    @SerializedName("listAttendance")
    val listAttendance: List<Attendance>,
    @SerializedName("lupaAbsenCount")
    val lupaAbsenCount: Int
)