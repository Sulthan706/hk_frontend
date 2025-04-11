package com.hkapps.hygienekleen.features.features_vendor.profile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateProfileResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("errorCode")
    @Expose
    val errorCode: String,
    val data: DataUpdateProfile
)