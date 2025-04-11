package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.service

import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient.CalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.datesrkbclient.DatesRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.detailjobrkbclient.DetailJobRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.eventcalendarrkbclient.EventCalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.homerkbclient.HomeRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MonthlyWorkClientService {

    @GET("api/v1/client/rkb/home")
    fun getHomeRkbClient(
        @Query("projectCode") projectCode: String
    ): Single<HomeRkbClientResponseModel>

    @GET("api/v1/client/rkb/dates")
    fun getDatesRkbClient(
        @Query("projectCode") projectCode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): Single<DatesRkbClientResponseModel>

    @GET("api/v1/client/rkb/calendar/events")
    fun getEventCalendarRkbClient(
        @Query("projectCode") projectCode: String,
        @Query("clientId") clientId: Int,
        @Query("month") month: String,
        @Query("year") year: String
    ): Single<EventCalendarRkbClientResponseModel>

    @GET("api/v1/client/rkb/calendar")
    fun getCalendarRkbClient(
        @Query("clientId") clientId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage:Int
    ): Single<CalendarRkbClientResponseModel>

    @GET("api/v1/client/rkb/fetch")
    fun getDetailRkbClient(
        @Query("idJobs") idJobs: Int
    ): Single<DetailJobRkbClientResponseModel>

    @GET("api/v1/client/rkb/status")
    fun getListByStatsRkbClient(
        @Query("clientId") clientId: Int,
        @Query("projectCode") projectCode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("locationId") locationId : Int
    ): Single<ListByStatsRkbClientResponseModel>

}