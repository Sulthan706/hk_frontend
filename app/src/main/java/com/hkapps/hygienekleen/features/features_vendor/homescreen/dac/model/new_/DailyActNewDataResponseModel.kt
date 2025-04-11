package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActNewDataResponseModel(
    @SerializedName("employee")
    @Expose
    val employeeDataResponseModel: DailyActEmployeeResponseModel,
    @SerializedName("plotting")
    @Expose
    val plottingDataResponseModel: DailyActPlottingResponseModel
)
