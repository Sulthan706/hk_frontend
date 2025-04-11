package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MidLevelOneSchDataResponseModel(
    @SerializedName("idScheduleLeader")
    @Expose
    val idScheduleLeader: Int,
    @SerializedName("idEmployee")
    @Expose
    val idEmployee: Int,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("monthLeader")
    @Expose
    val monthLeader: Int,
    @SerializedName("yearLeader")
    @Expose
    val yearLeader: Int,
    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,
    @SerializedName("idDetailShift")
    @Expose
    val idDetailShift: Int,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("endAt")
    @Expose
    val endAt: String,
    @SerializedName("latitude")
    @Expose
    val latitude: String,
    @SerializedName("longitude")
    @Expose
    val longitude: String,
    @SerializedName("radius")
    @Expose
    val radius: Int
)