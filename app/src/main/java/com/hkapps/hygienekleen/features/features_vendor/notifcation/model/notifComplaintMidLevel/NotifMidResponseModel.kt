package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotifMidResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val dataNotifResponseModel: DataNotifMidResponseModel
)