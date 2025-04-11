package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel.MidScheduleResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleService {

    @GET("/api/v1/calendar/employee")
    fun getScheduleService(
        @Query("employeeId") employeeId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
    ): Single<ScheduleResponseModel>


    @GET("/api/v1/calendar/employee")
    fun getMidScheduleService(
        @Query("employeeId") employeeId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
    ): Single<MidScheduleResponseModel>
}