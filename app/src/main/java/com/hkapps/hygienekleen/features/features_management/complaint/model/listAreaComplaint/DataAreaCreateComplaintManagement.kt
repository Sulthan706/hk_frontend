package com.hkapps.hygienekleen.features.features_management.complaint.model.listAreaComplaint

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataAreaCreateComplaintManagement(
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("locationName")
    @Expose
    val locationName: String
)