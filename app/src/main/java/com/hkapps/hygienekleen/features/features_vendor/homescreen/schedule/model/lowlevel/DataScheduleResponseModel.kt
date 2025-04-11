package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataScheduleResponseModel(
    @SerializedName("content")
    @Expose
    val content: List<DataArrayContent>,
    @SerializedName("pageable")
    @Expose
    val pageable: DataPageable,
    @SerializedName("size")
    @Expose
    val size: Int,
    @SerializedName("number")
    @Expose
    val number: Int,
    @SerializedName("sort")
    @Expose
    val sort: DataSort,
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