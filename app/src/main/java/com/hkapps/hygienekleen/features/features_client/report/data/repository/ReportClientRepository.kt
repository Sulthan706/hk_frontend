package com.hkapps.hygienekleen.features.features_client.report.data.repository

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

interface ReportClientRepository {

    fun getListShiftReport(
        projectId: String
    ): Single<ListShiftReportClientResponse>

    fun getListAreaReport(
        projectCode: String,
        shiftId: Int,
        page: Int
    ): Single<ListAreaReportClientReponse>

    fun getDetailAreaReport(
        projectCode: String,
        shiftId: Int,
        plottingId: Int
    ): Single<DetailAreaReportClientResponse>

    fun getDetailPlottingReport(
        plottingId: Int
    ): Single<DetailPlottingReportResponse>

    fun getListLocationReport(
        projectCode: String
    ): Single<ListLocationResponseModel>

    fun getListSubLocationReport(
        projectCode: String,
        locationId: Int
    ): Single<ListSublocationResponseModel>

    fun getListShiftReports(
        projectCode: String,
        locationId: Int,
        subLocationId: Int
    ): Single<ListShiftReportResponseModel>

    fun getWorkHourReport(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<WorkHourReportResponseModel>

    fun getListKondisiArea(
        projectCode: String,
        page: Int
    ): Single<ListKondisiAreaResponseModel>

    fun getDashboardReport(
        projectCode: String,
        month: String,
        year: String
    ): Single<DashboardReportResponseModel>

    fun getReportAbsensi(
        projectCode: String,
        date: String
    ): Single<ReportAbsensiResponseModel>

    fun getDetailKondisiArea(
        projectCode: String,
        locationId: Int,
        subLocationId: Int,
        shiftId: Int,
        date: String
    ): Single<DetailKondisiAreaResponseModel>


}