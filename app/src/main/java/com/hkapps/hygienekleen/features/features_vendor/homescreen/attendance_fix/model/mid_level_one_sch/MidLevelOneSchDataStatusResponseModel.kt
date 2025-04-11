package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MidLevelOneSchDataStatusResponseModel(
    @SerializedName("employeeId")
    @Expose
    val employeeId: Int,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("idScheduleLeader")
    @Expose
    val idScheduleLeader: Int,
    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,
    @SerializedName("statusAttendance")
    @Expose
    val statusAttendance: String,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("endAt")
    @Expose
    val endAt: String
)