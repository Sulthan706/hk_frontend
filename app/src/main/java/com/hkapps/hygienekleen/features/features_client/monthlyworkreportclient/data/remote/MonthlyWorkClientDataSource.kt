package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.remote

import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient.CalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.datesrkbclient.DatesRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.detailjobrkbclient.DetailJobRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.eventcalendarrkbclient.EventCalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.homerkbclient.HomeRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientResponseModel
import io.reactivex.Single

interface MonthlyWorkClientDataSource {

    fun getHomeRkbClient(
        projectCode: String
    ): Single<HomeRkbClientResponseModel>

    fun getDatesRkbClient(
        projectCode: String,
        startDate: String,
        endDate: String,
    ): Single<DatesRkbClientResponseModel>

    fun getEventCalendarRkbClient(
        projectCode: String,
        clientId: Int,
        month: String,
        year: String
    ): Single<EventCalendarRkbClientResponseModel>

    fun getCalendarRkbClient(
        clientId: Int,
        projectCode: String,
        date: String,
        page: Int,
        perPage:Int
    ): Single<CalendarRkbClientResponseModel>

    fun getDetailRkbClient(
        idJobs: Int
    ): Single<DetailJobRkbClientResponseModel>

    fun getListByStatsRkbClient(
        clientId: Int,
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int
    ): Single<ListByStatsRkbClientResponseModel>

}