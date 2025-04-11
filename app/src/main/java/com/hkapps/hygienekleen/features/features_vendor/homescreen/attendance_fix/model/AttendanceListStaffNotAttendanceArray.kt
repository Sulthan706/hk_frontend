package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceListStaffNotAttendanceArray (
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
    val employeePhoneNumber: List<AttendanceListStaffNotAttendanceNumber>,
    @SerializedName("employeePhotoProfile")
    @Expose
    val employeePhotoProfile: String,
    @SerializedName("employeeEmail")
    @Expose
    val employeeEmail: String,
    @SerializedName("employeeNik")
    @Expose
    val employeeNik: String,
    @SerializedName("jobCode")
    @Expose
    val jobCode: String,
    @SerializedName("jobName")
    @Expose
    val jobName: String,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("beginDate")
    @Expose
    val beginDate: String,
    @SerializedName("endDate")
    @Expose
    val endDate: String,
    @SerializedName("job")
    @Expose
    val `job`: AttendanceListStaffNotAttendanceJobData
)