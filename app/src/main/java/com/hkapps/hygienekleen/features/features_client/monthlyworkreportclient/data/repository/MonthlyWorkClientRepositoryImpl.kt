package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.repository

import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.remote.MonthlyWorkClientDataSource
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient.CalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.datesrkbclient.DatesRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.detailjobrkbclient.DetailJobRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.eventcalendarrkbclient.EventCalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.homerkbclient.HomeRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientResponseModel
import io.reactivex.Single
import javax.inject.Inject

class MonthlyWorkClientRepositoryImpl @Inject constructor(private val dataSource: MonthlyWorkClientDataSource):
MonthlyWorkClientRepository {
    override fun getHomeRkbClient(
        projectCode: String
    ): Single<HomeRkbClientResponseModel> {
        return dataSource.getHomeRkbClient(projectCode)
    }

    override fun getDatesRkbClient(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<DatesRkbClientResponseModel> {
        return dataSource.getDatesRkbClient(projectCode, startDate, endDate)
    }

    override fun getEventCalendarRkbClient(
        projectCode: String,
        clientId: Int,
        month: String,
        year: String
    ): Single<EventCalendarRkbClientResponseModel> {
        return dataSource.getEventCalendarRkbClient(projectCode, clientId, month, year)
    }

    override fun getCalendarRkbClient(
        clientId: Int,
        projectCode: String,
        date: String,
        page: Int,
        perPage: Int
    ): Single<CalendarRkbClientResponseModel> {
        return dataSource.getCalendarRkbClient(clientId, projectCode, date, page, perPage)
    }

    override fun getDetailRkbClient(idJobs: Int): Single<DetailJobRkbClientResponseModel> {
        return dataSource.getDetailRkbClient(idJobs)
    }

    override fun getListByStatsRkbClient(
        clientId: Int,
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int
    ): Single<ListByStatsRkbClientResponseModel> {
        return dataSource.getListByStatsRkbClient(clientId, projectCode, startDate, endDate, filterBy, page, perPage,locationId)
    }


}