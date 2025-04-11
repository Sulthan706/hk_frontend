package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MidDataSort(
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