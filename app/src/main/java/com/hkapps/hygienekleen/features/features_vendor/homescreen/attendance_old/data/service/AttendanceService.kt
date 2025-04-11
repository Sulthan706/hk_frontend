package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface AttendanceService {

    @GET("/api/v1/employee/activities")
    fun getDailyDataApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
    ): Single<DailyActResponseModel>

    @GET("/metadata/{params}")
    fun getChooseStaffDataApi(
        @Path("params") params: String,
    ): Single<ChooseStaffResponseModel>


    //GET QRCODE
    @GET("/api/v1/barcode/employee")
    fun getQRCodeDataApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<QRCodeResponseModel>


    //POST SELFIE
    @Multipart
    @POST("/api/v1/employee/attendance/in")
    fun postImageSelfieApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("barcodeKey") barcodeKey: String,
        @Part file: MultipartBody.Part,
    ): Single<SelfiePostResponseModel>


    //PUT SELFIE
    @Multipart
    @PUT("/api/v1/employee/attendance/out")
    fun putImageSelfieApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("barcodeKey") barcodeKey: String,
        @Part file: MultipartBody.Part,
    ): Single<SelfiePostResponseModel>

    //GET history absen
    @GET("/api/v1/employee/attendance/status")
    fun getAttendanceStatusApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<AttendanceStatusResponseModel>

    //GET history absen
    @GET("/api/v1/employee/attendance/history")
    fun getHistoryApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<HistoryResponseModel>

    @GET("/api/v1/employee/attendance/history/date")
    fun getHistoryByDateApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("datePrefix") datePrefix: String,
        @Query("dateSuffix") dateSuffix: String,
    ): Single<HistoryByDateResponseModel>

}