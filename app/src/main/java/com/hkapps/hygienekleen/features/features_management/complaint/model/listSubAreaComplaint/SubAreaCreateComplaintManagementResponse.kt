package com.hkapps.hygienekleen.features.features_management.complaint.model.listSubAreaComplaint

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubAreaCreateComplaintManagementResponse(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val data: ArrayList<DataSubAreaCreateComplaintManagement>
)