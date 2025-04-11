package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataMidSort(
    @SerializedName("empty")
    @Expose
    val empty: Boolean,
    @SerializedName("sorted")
    @Expose
    val sorted: Boolean,
    @SerializedName("unsorted")
    @Expose
    val unsorted: Boolean
)