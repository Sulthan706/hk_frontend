package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceListStaffAlreadyAttendanceArray(
    @SerializedName("idEmployee")
    @Expose
    val idEmployee: Int,
    @SerializedName("employeeCode")
    @Expose
    val employeeCode: String,
    @SerializedName("employeeName")
    @Expose
    val employeeName: String,
    @SerializedName("employeePhoneNumber")
    @Expose
    val employeePhoneNumber: String,
    @SerializedName("employeePhotoProfile")
    @Expose
    val employeePhotoProfile: String,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("jobCode")
    @Expose
    val jobCode: String,
    @SerializedName("scheduleType")
    @Expose
    val scheduleType:String,
    @SerializedName("statusAttendance")
    @Expose
    val statusAttendance:String,
    @SerializedName("attendanceInfo")
    @Expose
    val attendanceInfo: AttendanceInfoAlready


)

class AttendanceInfoAlready(
    @SerializedName("scanIn")
    @Expose
    val scanIn: String,
    @SerializedName("employeeImgSelfieIn")
    @Expose
    val employeeImgSelfieIn: String
)