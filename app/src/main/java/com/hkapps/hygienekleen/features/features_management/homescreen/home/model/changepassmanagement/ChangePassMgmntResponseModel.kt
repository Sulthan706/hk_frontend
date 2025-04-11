package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.changepassmanagement

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePassMgmntResponseModel (
    @SerializedName("oldPassword")
    @Expose
    val oldPassword: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("confirmPassword")
    @Expose
    val confirmPassword: String
)