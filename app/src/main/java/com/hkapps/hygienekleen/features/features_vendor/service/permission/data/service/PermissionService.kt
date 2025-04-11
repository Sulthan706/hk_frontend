package com.hkapps.hygienekleen.features.features_vendor.service.permission.data.service

import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CheckCreatePermissionResponse
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CreatePermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.HistoryPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.*
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.historyMid.HistoryPermissionMidResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface PermissionService {

    @Multipart
    @POST("/api/v2/permission/create")
    fun postPermission(
        @Part file: MultipartBody.Part,
        @Query("requestBy") requestBy: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("fromDate") fromDate: String,
        @Query("endDate") endDate: String,
    ): Single<CreatePermissionResponseModel>

    @GET("/api/v2/permission/history")
    fun getHistoryPermission(
        @Query("projectId") projectId: String,
        @Query("employeeId") employeeId: Int,
        @Query("page") page: Int
    ): Single<HistoryPermissionResponseModel>

    @GET("/api/v2/permission/process/all")
    fun getPermission(
        @Query("projectId") projectId: String,
        @Query("employeeId") employeeId: Int,
        @Query("jabatan") jabatan: String,
        @Query("page") page: Int
    ): Single<PermissionResponseModel>

    @GET("/api/v2/permission/process/employee/replace")
    fun getOperatorPermission(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int
    ): Single<OperatorPermissionResponseModel>

    @PUT("/api/v2/permission/process/accept")
    fun putPermission(
        @Query("permissionId") requestBy: Int,
        @Query("employeeApproveId") projectId: Int,
        @Query("employeeReplaceId") title: Int,
        @Query("projectId") description: String,
        @Query("date") date: String,
    ): Single<PutPermissionResponseModel>

    @PUT("/api/v2/permission/process/deny/{permissionId}")
    fun putDenialPermission(
        @Path("permissionId") id: Int
    ): Single<PutDenialPermissionResponseModel>

    @PUT("/api/v2/permission/process/accept")
    fun putPermissionNew(
        @Query("permissionId") requestBy: Int,
        @Query("employeeApproveId") projectId: Int,
        @Query("projectId") description: String,
    ): Single<PutPermissionResponseModel>

    @PUT("/api/v2/permission/process/deny/{permissionId}")
    fun putDenialPermissionNew(
        @Path("permissionId") id: Int
    ): Single<PutDenialPermissionResponseModel>

    @GET("/api/v2/permission/{id}")
    fun getDetailPermission(
        @Path("id") id: Int
    ): Single<DetailPermissionResponse>

    @GET("/api/v2/permission/check")
    fun getCheckPermission(
        @Query("employeeId") employeeId: Int
    ): Single<CheckCreatePermissionResponse>

    @GET("/api/v2/permission/history/user")
    fun getHistoryPermissionMid(
        @Query("employeeId") employeeId: Int
    ): Single<HistoryPermissionMidResponse>

    @GET("/api/v2/permission/history/date")
    fun getHistoryDateMid(
        @Query("employeeId") employeeId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<HistoryPermissionMidResponse>

    @Multipart
    @POST("/api/v2/permission/upload/image")
    fun uploadPhotoPermission(
        @Query("permissionId") permissionId: Int,
        @Part file: MultipartBody.Part
    ): Single<DetailPermissionResponse>

}