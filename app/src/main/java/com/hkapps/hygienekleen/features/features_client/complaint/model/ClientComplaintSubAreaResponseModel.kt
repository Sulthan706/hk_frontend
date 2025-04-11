package com.hkapps.hygienekleen.features.features_client.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClientComplaintSubAreaResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val data: ArrayList<DataArrayClientComplaintSubAreaResponseModel>
)