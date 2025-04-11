package com.hkapps.hygienekleen.features.features_client.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataArrayClientComplaintSubAreaResponseModel(
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String
)