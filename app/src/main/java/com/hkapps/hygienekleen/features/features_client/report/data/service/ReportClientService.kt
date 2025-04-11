package com.hkapps.hygienekleen.features.features_client.report.data.service

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
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportClientService {

    @GET("/api/v1/checklist/shift")
    fun getListShiftReport(
        @Query("projectId") projectId: String
    ): Single<ListShiftReportClientResponse>

    @GET("/api/v1/project/shift/area")
    fun getListAreaReport(
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int,
        @Query("page") page: Int
    ): Single<ListAreaReportClientReponse>

    @GET("/api/v1/project/plotting/area")
    fun getDetailAreaReport(
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int,
        @Query("plottingId") plottingId: Int
    ): Single<DetailAreaReportClientResponse>

    @GET("/api/v1/checklist/plotting")
    fun getDetailPlottingReport(
        @Query("plottingId") plottingId: Int
    ): Single<DetailPlottingReportResponse>
    //list location
    @GET("/api/v1/client/list-location")
    fun getListLocationReport(
        @Query("projectCode") projectCode: String
    ): Single<ListLocationResponseModel>
    //list sublocation
    @GET("/api/v1/client/list-sub-location")
    fun getListSubLocationReport(
        @Query("projectCode") projectCode: String,
        @Query("locationId") locationId: Int
    ): Single<ListSublocationResponseModel>
    //list shift
    @GET("/api/v1/client/list-shift")
    fun getListShiftReports(
        @Query("projectCode") projectCode: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int
    ): Single<ListShiftReportResponseModel>
    //get data jadwal kerja
    @GET("/api/v1/client/work-page-team")
    fun getWorkHourReport(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
    ) :Single<WorkHourReportResponseModel>
    //pagination list kondisi area
    @GET("/api/v1/client/report/list-location")
    fun getListKondisiArea(
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int
    ): Single<ListKondisiAreaResponseModel>
    //main screen report
    @GET("/api/v1/client/report/main-report")
    fun getDashboardReport(
        @Query("projectCode") projectCode: String,
        @Query("month") month: String,
        @Query("year") year: String
    ): Single<DashboardReportResponseModel>
    //main api report absensi
    @GET("/api/v1/client/attendance-history-page")
    fun getReportAbsensi(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String
    ):Single<ReportAbsensiResponseModel>
    //main detail kondisi area
    @GET("/api/v1/client/report/area-condition")
    fun getDetailKondisiArea(
        @Query("projectCode") projectCode: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Query("shiftId") shiftId:Int,
        @Query("date") date: String
    ): Single<DetailKondisiAreaResponseModel>

}