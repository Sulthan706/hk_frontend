package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel

import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.DataArrayContent
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataNotifMidResponseModel(
    @SerializedName("content")
    @Expose
//    val content: List<DataMidArrayContent>,
    val content: List<DataArrayContent>,
    @SerializedName("pageable")
    @Expose
    val pageable: DataMidPageable,
    @SerializedName("size")
    @Expose
    val size: Int,
    @SerializedName("number")
    @Expose
    val number: Int,
    @SerializedName("sort")
    @Expose
    val sort: DataMidSort,
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