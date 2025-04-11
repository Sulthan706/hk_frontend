package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.readnotifmid


import com.google.gson.annotations.SerializedName

data class ReadNotificationMidResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)