package com.hkapps.hygienekleen.features.features_vendor.profile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePassRequestModel(
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