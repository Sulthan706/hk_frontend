package com.hkapps.hygienekleen.features.auth.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataClient(
    @SerializedName("idEmployeeProject")
    @Expose
    val idEmployeeProject: Int,
    @SerializedName("employeeNuc")
    @Expose
    val employeeNuc: String,
    @SerializedName("employeeName")
    @Expose
    val employeeName: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("refreshToken")
    @Expose
    val refreshToken: String,
    @SerializedName("clientId")
    @Expose
    val clientId: Int,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("clientName")
    @Expose
    val clientName: String
)