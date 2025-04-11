package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MidDataScheduleResponseModel(
    @SerializedName("content")
    @Expose
    val contentMid: List<MidDataArrayContent>,
    @SerializedName("pageable")
    @Expose
    val pageableMid: MidDataPageable,
    @SerializedName("size")
    @Expose
    val size: Int,
    @SerializedName("number")
    @Expose
    val number: Int,
    @SerializedName("sort")
    @Expose
    val sortMid: MidDataSort,
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