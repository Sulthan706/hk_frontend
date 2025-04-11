package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.model.PlottingResponseModel
import io.reactivex.Single

interface PlottingRepository {
    fun getPlotting(params: String): Single<PlottingResponseModel>
}