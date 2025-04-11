package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.changepassmanagement

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePassManagementResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("errorCode")
    @Expose
    val errorCode: String,
    @SerializedName("message")
    @Expose
    val message: String
)