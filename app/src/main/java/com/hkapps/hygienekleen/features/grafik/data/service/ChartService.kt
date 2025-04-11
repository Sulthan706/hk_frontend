package com.hkapps.hygienekleen.features.grafik.data.service

import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.ListTimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.ListTurnOverResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.TurnOverResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ChartService {

    @GET("api/v1/project/home/absen")
    fun getDataAbsenceStaff(
        @Query("projectCode") projectCode : String
    ):Single<ChartAbsenceStaffResponse>

    @GET("api/v1/project/home/timesheet")
    fun getDataTimeSheet(
        @Query("projectCode") projectCode : String
    ):Single<TimeSheetResponse>

    @GET("api/v1/project/home/timesheet/list")
    fun getDataListTimeSheet(
        @Query("projectCode") projectCode : String
    ):Single<ListTimeSheetResponse>

    @GET("api/v1/turnover/home")
    fun getDataTurnOver(
        @Query("projectCode") projectCode : String
    ):Single<TurnOverResponse>

    @GET("api/v1/turnover/home/list-turnover")
    fun getListDataTurnOver(
        @Query("projectCode") projectCode : String
    ):Single<ListTurnOverResponse>



}