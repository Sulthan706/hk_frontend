package com.hkapps.hygienekleen.features.features_vendor.profile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileDataResponseModel(
    @SerializedName("idEmployee")
    @Expose
    val idEmployee: Int,

    @SerializedName("employeeNuc")
    @Expose
    val employeeNuc: String,

    @SerializedName("employeeName")
    @Expose
    val employeeName: String,

    @SerializedName("employeePhoneNumber")
    @Expose
    val employeePhoneNumber: String,

    @SerializedName("employeeEmail")
    @Expose
    val employeeEmail: String,

    @SerializedName("employeePhotoProfile")
    @Expose
    val employeePhotoProfile: String,

    @SerializedName("employeeNik")
    @Expose
    val employeeNik: String,

    @SerializedName("jobCode")
    @Expose
    val jobCode: String,

    @SerializedName("jobName")
    @Expose
    val jobName: String,

    @SerializedName("jobLevel")
    @Expose
    val jobLevel: String,

    @SerializedName("job")
    @Expose
    val job: ProfileJobResponseModel,

    @SerializedName("project")
    @Expose
    val project: ProfileProjectResponseModel
)