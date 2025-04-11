package com.hkapps.hygienekleen.features.features_client.report.data.repository

import com.hkapps.hygienekleen.features.features_client.report.data.remote.ReportClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.report.model.dashboardreport.DashboardReportResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.detailArea.DetailAreaReportClientResponse
import com.hkapps.hygienekleen.features.features_client.report.model.detailPlotting.DetailPlottingReportResponse
import com.hkapps.hygienekleen.features.features_client.report.model.detailkondisiarea.DetailKondisiAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.WorkHourReportResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listArea.ListAreaReportClientReponse
import com.hkapps.hygienekleen.features.features_client.report.model.listShift.ListShiftReportClientResponse
import com.hkapps.hygienekleen.features.features_client.report.model.listkondisiarea.ListKondisiAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listlocation.ListLocationResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listshiftreport.ListShiftReportResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listsublocation.ListSublocationResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.reportabsensi.ReportAbsensiResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ReportClientRepositoryImpl @Inject constructor(private val remoteDataSource: ReportClientRemoteDataSource):
    ReportClientRepository {

    override fun getListShiftReport(projectId: String): Single<ListShiftReportClientResponse> {
        return remoteDataSource.getListShiftReport(projectId)
    }

    override fun getListAreaReport(
        projectCode: String,
        shiftId: Int,
        page: Int
    ): Single<ListAreaReportClientReponse> {
        return remoteDataSource.getListAreaReport(projectCode, shiftId, page)
    }

    override fun getDetailAreaReport(
        projectCode: String,
        shiftId: Int,
        plottingId: Int
    ): Single<DetailAreaReportClientResponse> {
        return remoteDataSource.getDetailAreaReport(projectCode, shiftId, plottingId)
    }

    override fun getDetailPlottingReport(plottingId: Int): Single<DetailPlottingReportResponse> {
        return remoteDataSource.getDetailPlottingReport(plottingId)
    }

    override fun getListLocationReport(projectCode: String): Single<ListLocationResponseModel> {
        return remoteDataSource.getListLocationReport(projectCode)
    }

    override fun getListSubLocationReport(
        projectCode: String,
        locationId: Int
    ): Single<ListSublocationResponseModel> {
        return remoteDataSource.getListSubLocationReport(projectCode, locationId)
    }

    override fun getListShiftReports(
        projectCode: String,
        locationId: Int,
        subLocationId: Int
    ): Single<ListShiftReportResponseModel> {
        return remoteDataSource.getListShiftReports(projectCode, locationId, subLocationId)
    }

    override fun getWorkHourReport(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<WorkHourReportResponseModel> {
        return remoteDataSource.getWorkHourReport(projectCode, date, shiftId, locationId, subLocationId)
    }

    override fun getListKondisiArea(
        projectCode: String,
        page: Int
    ): Single<ListKondisiAreaResponseModel> {
        return remoteDataSource.getListKondisiArea(projectCode, page)
    }

    override fun getDashboardReport(
        projectCode: String,
        month: String,
        year: String
    ): Single<DashboardReportResponseModel> {
        return remoteDataSource.getDashboardReport(projectCode, month, year)
    }

    override fun getReportAbsensi(
        projectCode: String,
        date: String
    ): Single<ReportAbsensiResponseModel> {
        return remoteDataSource.getReportAbsensi(projectCode, date)
    }

    override fun getDetailKondisiArea(
        projectCode: String,
        locationId: Int,
        subLocationId: Int,
        shiftId: Int,
        date: String
    ): Single<DetailKondisiAreaResponseModel> {
        return remoteDataSource.getDetailKondisiArea(projectCode, locationId, subLocationId, shiftId, date)
    }


}