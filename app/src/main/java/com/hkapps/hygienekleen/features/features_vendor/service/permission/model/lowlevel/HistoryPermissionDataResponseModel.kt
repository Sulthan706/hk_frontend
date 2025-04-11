package com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HistoryPermissionDataResponseModel(
    @SerializedName("content")
    @Expose
    val content: List<ListHistoryPermission>,
    @SerializedName("size")
    @Expose
    val size: Int,
    @SerializedName("number")
    @Expose
    val number: Int,
    @SerializedName("numberOfElements")
    @Expose
    val numberOfElements: Int,
    @SerializedName("first")
    @Expose
    val first: Boolean,
    @SerializedName("last")
    @Expose
    val last: Boolean,
    @SerializedName("empty")
    @Expose
    val empty: Boolean
)