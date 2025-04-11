package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActEmployeeResponseModel(
    @SerializedName("employeeId")
    @Expose
    val employeeId: Int,
    @SerializedName("employeeCode")
    @Expose
    val employeeCode: String,
    @SerializedName("employeeName")
    @Expose
    val employeeName: String,
    @SerializedName("jobCode")
    @Expose
    val jobCode: String,
    @SerializedName("jobName")
    @Expose
    val jobName: String,
    @SerializedName("namaPosition")
    @Expose
    val namaPosition: String,
    @SerializedName("attendanceImage")
    @Expose
    val attendanceImage: String,
)