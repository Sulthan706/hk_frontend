package com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.CheckStatusChiefResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyDetailTargetManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ListDailyTargetManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ClosingManagementService {

    @GET("api/v1/rkb/management/target-harian/fm/v2")
    fun getListDailyTargetManagement(
        @Query("adminMasterId") id : Int,
        @Query("date") date : String
    ): Single<ListDailyTargetManagementResponse>

    @GET("api/v1/rkb/management/target-harian/fm/detail")
    fun getDetailDailyTargetManagement(
        @Query("projectCode") id : String,
        @Query("date") date : String
    ): Single<DailyDetailTargetManagementResponse>

    @GET("api/v1/rkb/management/pengalihan-target-harian/fm")
    fun getListDiversionManagement(
        @Query("locationId") locationId : Int,
        @Query("projectCode") projectCode : String,
        @Query("date") date : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ):Single<DiversionResponse>

    @PUT("api/v1/rkb/management/submit-closing/fm")
    fun submitClosingManagement(
        @Query("projectCode") projectCode : String ,
        @Query("date") date : String,
        @Query("adminMasterId") userId : Int,
    ):Single<ClosingResponse>

    @Multipart
    @PUT("api/v1/rkb/management/diverted")
    fun divertedManagement(
        @Query("idJobs") idJob : Int ,
        @Query("adminMasterId") userId : Int,
        @Query("description") desc : String,
        @Query("date") date : String,
        @Query("divertedShift") diverted : Int,
        @Part file: MultipartBody.Part
    ):Single<ClosingResponse>

    @PUT("api/v1/rkb/management/generate-file-closing/fm")
    fun generateFileClosingManagement(
        @Query("projectCode") projectCode : String ,
        @Query("date") date : String,
        @Query("adminMasterId") userId : Int,
    ):Single<ClosingResponse>

    @PUT("api/v1/rkb/management/send-email-closing/fm")
    fun sendEmailClosingManagement(
        @Body sendEmailClosingRequest: SendEmailClosingRequest
    ):Single<ClosingResponse>

    @GET("api/v1/project/client/{projectCode}")
    fun getListClientClosing(
        @Path("projectCode") projectCode : String
    ):Single<ClientClosingResponse>

    @GET("api/v1/rkb/closing-history/chief-or-fm")
    fun getListClosingHistoryChief(
        @Query("projectCode") id : String,
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): Single<HistoryClosingResponse>

    @GET("api/v1/project/is-pengawas-not-existed")
    fun checkClosingChief(
        @Query("projectCode") projectCode: String
    ):Single<CheckStatusChiefResponse>

    @GET("api/v1/rkb/management/closing/checker")
    fun checkClosingPopupHome(
        @Query("adminMasterId") adminMasterId : Int
    ):Single<CheckStatusChiefResponse>
}