package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory


import com.google.gson.annotations.SerializedName

data class NotificationMidHistory(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<NotifDataHistory>,
    @SerializedName("status")
    val status: String
)