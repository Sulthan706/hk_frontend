package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old

import android.view.View
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.cachapa.expandablelayout.ExpandableLayout

data class DailyActDataArrayResponseModel(
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("locationName")
    @Expose
    val locationName: String,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String,
    @SerializedName("objectId")
    @Expose
    val objectId: String,
    @SerializedName("activity")
    @Expose
    val activity: String,
    @SerializedName("shiftDescription")
    @Expose
    val shiftDescription: String,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("endAt")
    @Expose
    val endAt: String,
    @SerializedName("machineName")
    @Expose
    val machineName: String,
    @SerializedName("toolName")
    @Expose
    val toolName: String,
    @SerializedName("chemicalName")
    @Expose
    val chemicalName: String,

//    @SerializedName("activity")
//    @Expose
//    val dailyActDataResponseModel: DailyActDataActivityResponseModel


    ) : View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

    override fun onClick(v: View?) {
    }

    override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
    }
}