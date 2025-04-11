package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.approvejob.ApproveJobRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.createba.CreateBaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.ListDailyTargetResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.detailchecklistrkb.DetailChecklistRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.escallationrkb.EscallationLowLevResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.eventcalendar.EventCalendarRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.homerkbnew.HomeRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.limitcreateba.LimitasiCreateBaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.monthrkb.MonthRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.rkboperator.GetStatsRkbOperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.statusapprovejobs.StatusApprovalJobResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb.TargetByDateRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.TargetStatusRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.uploadjob.succesupload.SuccessUploadPhotoResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface MonthlyWorkReportService {

     @GET("api/v1/rkb/home")
     fun getHomeRkb(
         @Query("projectCode") projectCode: String
     ): Single<HomeRkbResponseModel>

    @GET("api/v1/rkb/dates")
    fun getHomeListWork(
        @Query("projectCode") projectCode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<MonthRkbResponseModel>

    @GET("api/v1/rkb/status")
    fun getListStatusRkb(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("locationId") locationId : Int
    ): Single<TargetStatusRkbResponseModel>

    @GET("api/v1/rkb/calendar")
    fun getTargetByDateRkb(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<TargetByDateRkbResponseModel>

    @GET("api/v1/rkb/fetch")
    fun getDetailChecklistRkb(
        @Query("idJobs") idJobs:Int
    ): Single<DetailChecklistRkbResponseModel>

    @Multipart
    @PUT("api/v1/rkb/diverted")
    fun putCreateBaRkb(
        @Query("idJobs") idJobs: Int,
        @Query("employeeId") employeeId: Int,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("divertedShift") divertedShift : Int,
        @Part file: MultipartBody.Part
    ): Single<CreateBaResponseModel>

    @Multipart
    @PUT("api/v1/rkb/uploads")
    fun putUploadPhotoRkb(
        @Query("idJobs") idJobs: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part,
        @Query("uploadType") uploadType: String,
        @Query("comment") comment: String
    ): Single<SuccessUploadPhotoResponseModel>

    @GET("api/v1/rkb/calendar/events")
    fun getEventCalendarRkb(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("month") month: String,
        @Query("year") year: String
    ): Single<EventCalendarRkbResponseModel>

    @PUT("api/v1/rkb/approve")
    fun putApproveJobRkb(
        @Query("idJobs") idJobs: Int,
        @Query("employeeId") employeeId: Int
    ): Single<ApproveJobRkbResponseModel>

    @GET("api/v1/rkb/check")
    fun getStatusApproveJob(
        @Query("employeeId") employeeId: Int
    ): Single<StatusApprovalJobResponseModel>

    @GET("api/v1/rkb/escalation")
    fun getEscalationRkb(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ): Single<EscallationLowLevResponseModel>

    @GET("api/v1/rkb/validate-berita-acara?")
    fun getLimitasiCreateBa(
        @Query("employeeId") employeeId: Int,
        @Query("idJobs") idJobs: Int
    ): Single<LimitasiCreateBaResponseModel>

    @GET("api/v1/rkb/operator-rkb")
    fun getStatsRkbOperator(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ): Single<GetStatsRkbOperatorResponseModel>

    @GET("api/v1/rkb/target-harian")
    fun getRkbDailyTarget(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ):Single<DailyTargetResponse>

    @GET("api/v1/rkb/target-harian/v2")
    fun getRkbDailyTargetV2(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ):Single<ListDailyTargetResponse>


    @GET("api/v1/rkb/detail-target-harian")
    fun getDetailRkbDailyTarget(
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int,
        @Query("employeeId") employeeId: Int
    ):Single<DailyTargetResponse>







}