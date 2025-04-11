package com.hkapps.hygienekleen.features.features_client.home.data.service

import com.hkapps.hygienekleen.features.features_client.home.model.ProfileClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.badgenotif.BadgeNotifClient
import com.hkapps.hygienekleen.features.features_client.home.model.countChecklist.CountChecklistHomeClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.countComplaint.InfoComplaintClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.projectDashboard.ProjectInfoClientResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeClientService {

    @GET("/api/v1/client/profile/{clientId}")
    fun getProfileClient(@Path("clientId") userId: Int): Single<ProfileClientResponse>

    @GET("/api/v1/complaint/countComplaintClient")
    fun getInfoComplaintClient(
        @Query("projectCode") projectId: String,
        @Query("clientId") userId: Int
    ): Single<InfoComplaintClientResponse>

    @GET("api/v1/checklist/project/count/info/{projectCode}")
    fun getCountChecklistClient(
        @Path("projectCode") projectCode: String
    ): Single<CountChecklistHomeClientResponse>

    @GET("api/v1/homeclient/getProject")
    fun getProjectInfo(
        @Query("projectCode") projectCode: String
    ): Single<ProjectInfoClientResponse>

    @GET("api/v1/client/notif/count")
    fun getCountNotifClient(
        @Query("clientId") clientId: Int,
        @Query("projectCode") projectCode: String
    ): Single<BadgeNotifClient>

    @GET("api/v1/project/home/absen")
    fun getDataAbsenceStaff(
        @Query("projectCode") projectCode : String
    ):Single<ChartAbsenceStaffResponse>

    @GET("api/v1/project/home/timesheet")
    fun getDataTimeSheet(
        @Query("projectCode") projectCode : String
    ):Single<TimeSheetResponse>

    @GET("api/v1/materialrequest/home/list")
    fun getDataMR(
        @Query("projectCode") projectCode : String,
        @Query("month") month : Int,
        @Query("year") year : Int,
        @Query("page") page : Int,
        @Query("perPage") size : Int
    ):Single<ItemMrResponse>

    @GET("api/v1/mesin/home/list-mesin")
    fun getDataListBAKMachine(
        @Query("projectCode") projectCode : String,
        @Query("page") page : Int,
        @Query("perPage") size : Int,
    ):Single<BakMachineResponse>





}