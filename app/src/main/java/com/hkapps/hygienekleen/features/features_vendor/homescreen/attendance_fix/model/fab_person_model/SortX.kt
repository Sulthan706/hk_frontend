package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model


import com.google.gson.annotations.SerializedName

data class SortX(
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)