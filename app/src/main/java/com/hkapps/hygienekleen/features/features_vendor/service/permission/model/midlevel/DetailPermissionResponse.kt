package com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailPermissionResponse (
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val data: DetailDataPermission
)