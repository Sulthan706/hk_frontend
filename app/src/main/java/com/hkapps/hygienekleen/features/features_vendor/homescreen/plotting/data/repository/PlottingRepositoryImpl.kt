package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.remote.PlottingRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.model.PlottingResponseModel
import io.reactivex.Single
import javax.inject.Inject

class PlottingRepositoryImpl @Inject constructor(private val remoteDataSource: PlottingRemoteDataSource) :
    PlottingRepository {
    override fun getPlotting(params: String): Single<PlottingResponseModel> {
        return remoteDataSource.getPlottingData(params)
    }
}