package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChooseStaffResponseModel(
    @SerializedName("created")
    @Expose
    val created: Int,
    @SerializedName("files")
    @Expose
    val chooseStaffDataResponseModel: ArrayList<ChooseStaffDataResponseModel>,
)