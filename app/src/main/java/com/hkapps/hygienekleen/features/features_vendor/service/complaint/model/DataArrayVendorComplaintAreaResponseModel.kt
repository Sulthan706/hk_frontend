package com.hkapps.hygienekleen.features.features_vendor.service.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataArrayVendorComplaintAreaResponseModel(
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