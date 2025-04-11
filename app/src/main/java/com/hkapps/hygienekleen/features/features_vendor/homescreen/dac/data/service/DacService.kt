package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActNewResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.CheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.CheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.DACCheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.PutCheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import retrofit2.http.*

interface DacService {
    @GET("/api/v1/employee/activities")
    fun getDailyDataApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
    ): Single<DailyActResponseModel>

    @GET("/api/v1/employee/dac")
    fun getDailyDataNewApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int
    ): Single<DailyActNewResponseModel>

    @GET("/api/v1/employee/dac")
    fun getCountDACApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int
    ): Single<DACCheckResponseModel>

    @GET("/api/v1/employee/dac/status/checklist")
    fun getChecklistDacLowLevelApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int,
        @Query("activityId") activityId: Int,
        @Query("plottingId") plottingId: Int
    ): Single<CheckDACResponseModel>

    @POST("/api/v1/employee/dac/check")
    fun postChecklistDacLowApi(
        @Query("employeeId") employeeId: Int,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int,
        @Query("projectId") projectId: String,
        @Query("plottingId") plottingId: Int,
        @Query("shiftId") shiftId: Int,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Query("activityId") activityId: Int
    ): Single<CheckResponseModel>


    @PUT("/api/v1/employee/dac/confirm")
    fun putChecklistDacLowApi(
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int
    ): Single<PutCheckDACResponseModel>
}