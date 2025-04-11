package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.service.PlottingService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.model.PlottingResponseModel
import io.reactivex.Single
import javax.inject.Inject

class PlottingRemoteDataSourceImpl @Inject constructor(private val service: PlottingService):
    PlottingRemoteDataSource {
    override fun getPlottingData(params: String): Single<PlottingResponseModel> {
        return service.getPlottingService(1,"1")
    }
}