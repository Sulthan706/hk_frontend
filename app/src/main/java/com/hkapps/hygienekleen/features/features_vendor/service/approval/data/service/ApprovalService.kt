package com.hkapps.hygienekleen.features.features_vendor.service.approval.data.service

import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.approvalAttendance.ApprovalAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.ListAttendanceApprovalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.HistoryUserFlyingResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApprovalService {

    // user flying approval
    @GET("/api/v1/user_flying/list")
    fun getListAttendanceApproval (
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ListAttendanceApprovalResponse>

    @POST("/api/v1/user_flying/refuse")
    fun submitDeclineAttendance (
        @Query("employeeId") employeeId: Int,
        @Query("idUserFlying") idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    @POST("/api/v1/user_flying/in/approve")
    fun submitApproveAttendanceIn(
        @Query("employeeId") employeeId: Int,
        @Query("idUserFlying") idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    @POST("/api/v1/user_flying/in/approve")
    fun submitApproveAttendanceOut(
        @Query("employeeId") employeeId: Int,
        @Query("idUserFlying") idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    @GET("/api/v1/user_flying/history")
    fun getHistoryUserFlying(
        @Query("employeeId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("date") date: String
    ): Single<HistoryUserFlyingResponse>


    // user flying approval management
    @POST("/api/v1/user_flying/management/refuse")
    fun submitDeclineAttendanceManagement (
        @Query("adminMasterId") userId: Int,
        @Query("idUserFlying") idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    @POST("/api/v1/user_flying/management/in/approve")
    fun submitApproveAttendanceInManagement(
        @Query("adminMasterId") userId: Int,
        @Query("idUserFlying") idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    @POST("/api/v1/user_flying/management/out/approve")
    fun submitApproveAttendanceOutManagement(
        @Query("adminMasterId") userId: Int,
        @Query("idUserFlying") idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    @GET("/api/v1/user_flying/management/list")
    fun getListAttendanceApprovalManagement(
        @Query("adminMasterId") userId: Int,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ListAttendanceApprovalResponse>

    @GET("/api/v1/user_flying/management/history")
    fun getHistoryUserFlyingManagement(
        @Query("adminMasterId") userId: Int,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("date") date: String
    ): Single<HistoryUserFlyingResponse>

}