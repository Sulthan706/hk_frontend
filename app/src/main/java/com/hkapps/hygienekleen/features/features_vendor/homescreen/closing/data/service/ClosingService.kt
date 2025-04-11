package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.service


import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignmentResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ListClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift.DetailShiftResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClosingService {

    @GET("api/v1/rkb/pengalihan-target-harian")
    fun geListDailyDiversion(
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int,
        @Query("locationId") locationId: Int,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): Single<DiversionResponse>

    @GET("api/v1/rkb/area-assignment")
    fun getAreaAssignment(
        @Query("projectCode") projectCode: String,
        @Query("category") category : String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): Single<AreaAssignmentResponse>

    @GET("api/v1/rkb/closing-history")
    fun getHistoryClosing(
        @Query("employeeId") employeeId: Int,
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ):Single<HistoryClosingResponse>

    @GET("api/v1/rkb/shift-pengalihan")
    fun getShiftDiversion(
        @Query("idShift") idShift : Int,
        @Query("projectCode") projectCode : String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ):Single<DetailShiftResponse>

    @PUT("api/v1/rkb/submit-closing")
    fun submitDailyClosing(
        @Query("idDetailEmployeeProject") idDetailEmployeeProject : Int,
        @Query("employeeId") employeeId : Int,
    ): Single<ClosingResponse>


    @GET("api/v1/rkb/list-closings")
    fun listClosing(
        @Query("projectCode") projectCode: String,
        @Query("startAt") startDate: String,
        @Query("endAt") endDate: String,
        @Query("roles") rolesOne : String,
        @Query("roles") rolesTwo : String,
        @Query("status") status : String,
        @Query("page") page: Int,
        @Query("size") perPage: Int,
    ):Single<ListClosingResponse>

    @GET("api/v1/rkb/target-harian/chief")
    fun getDetailDailyTargetChief(
        @Query("projectCode") id : String,
        @Query("date") date : String,
        @Query("employeeId") employeeId : Int
    ): Single<DailyTargetResponse>

    @GET("api/v1/rkb/pengalihan-target-harian/chief")
    fun getListDiversionChief(
        @Query("locationId") locationId : Int,
        @Query("projectCode") projectCode : String,
        @Query("date") date : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ):Single<DiversionResponse>

    @PUT("api/v1/rkb/submit-closing/chief")
    fun submitClosingChief(
        @Query("projectCode") projectCode : String ,
        @Query("date") date : String,
        @Query("employeeId") userId : Int,
    ):Single<ClosingResponse>

    @PUT("api/v1/rkb/generate-file-closing/chief")
    fun generateFileClosingManagement(
        @Query("projectCode") projectCode : String ,
        @Query("date") date : String,
        @Query("employeeId") userId : Int,
    ):Single<ClosingResponse>

    @PUT("api/v1/rkb/send-email-closing/chief")
    fun sendEmailClosingManagement(
        @Body sendEmailClosingRequest: SendEmailClosingRequest
    ):Single<ClosingResponse>

    @PUT("api/v1/rkb/generate-file-closing/spv")
    fun generateFileClosingSPV(
        @Query("projectCode") projectCode : String ,
        @Query("date") date : String,
        @Query("employeeId") userId : Int,
    ):Single<ClosingResponse>

    @PUT("api/v1/rkb/send-email-closing/spv")
    fun sendEmailClosingSPV(
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
}