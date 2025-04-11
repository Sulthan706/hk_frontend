package com.hkapps.hygienekleen.features.features_management.complaint.model.listSubAreaComplaint

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataSubAreaCreateComplaintManagement(
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String
)