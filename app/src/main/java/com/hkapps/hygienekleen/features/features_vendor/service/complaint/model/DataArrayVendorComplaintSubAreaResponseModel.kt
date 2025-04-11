package com.hkapps.hygienekleen.features.features_vendor.service.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataArrayVendorComplaintSubAreaResponseModel(
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String
)