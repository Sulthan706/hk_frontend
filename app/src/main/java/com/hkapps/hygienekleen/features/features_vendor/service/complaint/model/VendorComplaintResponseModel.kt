package com.hkapps.hygienekleen.features.features_vendor.service.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VendorComplaintResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val dataClientComplaintResponseModel: DataVendorComplaintResponseModel
)