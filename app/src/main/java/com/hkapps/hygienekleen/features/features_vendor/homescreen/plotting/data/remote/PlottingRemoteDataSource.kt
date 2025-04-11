package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.model.PlottingResponseModel
import io.reactivex.Single

interface PlottingRemoteDataSource {
    fun getPlottingData(params: String): Single<PlottingResponseModel>
}