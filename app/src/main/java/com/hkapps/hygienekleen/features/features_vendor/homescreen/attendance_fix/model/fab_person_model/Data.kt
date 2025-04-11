package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("content")
    val content: List<Content>,
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: Pageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: SortX
)