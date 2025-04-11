package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory


import com.google.gson.annotations.SerializedName

data class NotificationMidResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)