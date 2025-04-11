package com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreatePermissionResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
//    @SerializedName("data")
//    @Expose
//    val data:
)