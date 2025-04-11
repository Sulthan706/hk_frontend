package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.remote

import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.service.MonthlyWorkClientService
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient.CalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.datesrkbclient.DatesRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.detailjobrkbclient.DetailJobRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.eventcalendarrkbclient.EventCalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.homerkbclient.HomeRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientResponseModel
import io.reactivex.Single
import javax.inject.Inject

class MonthlyWorkClientDataSourceImpl @Inject constructor(private val service: MonthlyWorkClientService):
MonthlyWorkClientDataSource {
    override fun getHomeRkbClient(
        projectCode: String
    ): Single<HomeRkbClientResponseModel> {
        return service.getHomeRkbClient(projectCode)
    }

    override fun getDatesRkbClient(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<DatesRkbClientResponseModel> {
        return service.getDatesRkbClient(projectCode, startDate, endDate)
    }

    override fun getEventCalendarRkbClient(
        projectCode: String,
        clientId: Int,
        month: String,
        year: String
    ): Single<EventCalendarRkbClientResponseModel> {
        return service.getEventCalendarRkbClient(projectCode, clientId, month, year)
    }

    override fun getCalendarRkbClient(
        clientId: Int,
        projectCode: String,
        date: String,
        page: Int,
        perPage: Int
    ): Single<CalendarRkbClientResponseModel> {
        return service.getCalendarRkbClient(clientId, projectCode, date, page, perPage)
    }

    override fun getDetailRkbClient(idJobs: Int): Single<DetailJobRkbClientResponseModel> {
        return service.getDetailRkbClient(idJobs)
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
        return service.getListByStatsRkbClient(clientId, projectCode, startDate, endDate, filterBy, page, perPage,locationId)
    }


}