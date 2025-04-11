package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.model.PlottingResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PlottingService {

    @GET("/api/v1/employee/activities")
    fun getPlottingService(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
    ): Single<PlottingResponseModel>
}