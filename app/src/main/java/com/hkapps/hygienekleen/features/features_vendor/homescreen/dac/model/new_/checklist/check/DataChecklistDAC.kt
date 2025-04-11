package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataChecklistDAC (
    @SerializedName("activityId")
    @Expose
    val activityId: Int,
    @SerializedName("idDetailEmployeeProject")
    @Expose
    val idDetailEmployeeProject: Int
)