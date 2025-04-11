package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_

import android.view.View
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.cachapa.expandablelayout.ExpandableLayout

private var isSelected = false

data class DailyActDataArrayResponseModel(
    @SerializedName("idSubLocationActivity")
    @Expose
    val idSubLocationActivity: Int,
    @SerializedName("objectId")
    @Expose
    val objectId: String,
    @SerializedName("objectIdSecond")
    @Expose
    val objectIdSecond: String,
    @SerializedName("objectIdThird")
    @Expose
    val objectIdThird: String,
    @SerializedName("objectIdFour")
    @Expose
    val objectIdFour: String,
    @SerializedName("objectIdFive")
    @Expose
    val objectIdFive: String,
    @SerializedName("activity")
    @Expose
    val activity: String,
    @SerializedName("shiftDescription")
    @Expose
    val shiftDescription: String,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("statusCheklistActivity")
    @Expose
    val statusCheklistActivity: String,
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

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    fun isSelected(): Boolean {
        return isSelected
    }

}