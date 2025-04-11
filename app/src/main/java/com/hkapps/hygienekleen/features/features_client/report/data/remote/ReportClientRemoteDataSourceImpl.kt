package com.hkapps.hygienekleen.features.features_client.report.data.remote

import com.hkapps.hygienekleen.features.features_client.report.data.service.ReportClientService
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

class ReportClientRemoteDataSourceImpl @Inject constructor(private val service: ReportClientService):
    ReportClientRemoteDataSource{

    override fun getListShiftReport(projectId: String): Single<ListShiftReportClientResponse> {
        return service.getListShiftReport(projectId)
    }

    override fun getListAreaReport(
        projectCode: String,
        shiftId: Int,
        page: Int
    ): Single<ListAreaReportClientReponse> {
        return service.getListAreaReport(projectCode, shiftId, page)
    }

    override fun getDetailAreaReport(
        projectCode: String,
        shiftId: Int,
        plottingId: Int
    ): Single<DetailAreaReportClientResponse> {
        return service.getDetailAreaReport(projectCode, shiftId, plottingId)
    }

    override fun getDetailPlottingReport(plottingId: Int): Single<DetailPlottingReportResponse> {
        return service.getDetailPlottingReport(plottingId)
    }

    override fun getListLocationReport(projectCode: String): Single<ListLocationResponseModel> {
        return service.getListLocationReport(projectCode)
    }

    override fun getListSubLocationReport(
        projectCode: String,
        locationId: Int
    ): Single<ListSublocationResponseModel> {
        return service.getListSubLocationReport(projectCode, locationId)
    }

    override fun getListShiftReports(
        projectCode: String,
        locationId: Int,
        subLocationId: Int
    ): Single<ListShiftReportResponseModel> {
        return service.getListShiftReports(projectCode, locationId, subLocationId)
    }

    override fun getWorkHourReport(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<WorkHourReportResponseModel> {
        return service.getWorkHourReport(projectCode, date, shiftId, locationId, subLocationId)
    }

    override fun getListKondisiArea(
        projectCode: String,
        page: Int
    ): Single<ListKondisiAreaResponseModel> {
        return service.getListKondisiArea(projectCode, page)
    }

    override fun getDashboardReport(
        projectCode: String,
        month: String,
        year: String
    ): Single<DashboardReportResponseModel> {
        return service.getDashboardReport(projectCode, month, year)
    }

    override fun getReportAbsensi(
        projectCode: String,
        date: String
    ): Single<ReportAbsensiResponseModel> {
        return service.getReportAbsensi(projectCode, date)
    }

    override fun getDetailKondisiArea(
        projectCode: String,
        locationId: Int,
        subLocationId: Int,
        shiftId: Int,
        date: String
    ): Single<DetailKondisiAreaResponseModel> {
        return service.getDetailKondisiArea(projectCode, locationId, subLocationId, shiftId, date)
    }



}