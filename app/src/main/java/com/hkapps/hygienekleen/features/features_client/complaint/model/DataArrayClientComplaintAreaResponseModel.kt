package com.hkapps.hygienekleen.features.features_client.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataArrayClientComplaintAreaResponseModel(
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