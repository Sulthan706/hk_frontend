package com.hkapps.hygienekleen.features.features_management.service.permission.data.service

import com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement.ApprovalPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.createPermission.PermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.detailPermission.DetailPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.historyPermissionManagement.PermissionsHistoryManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.processPermission.ProcessPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.statusCreatePermission.ValidatePermissionManagementResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface PermissionManagementService {

    @GET("/api/v2/permission/management/process/all")
    fun getApprovalPermission (
        @Query("adminMasterId") userId: Int,
        @Query("jabatan") jabatan: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ApprovalPermissionManagementResponse>

    @Multipart
    @POST("/api/v2/permission/management/create")
    fun postPermissionManagement(
        @Part file: MultipartBody.Part,
        @Query("requestBy") requestBy: Int,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("fromDate") fromDate: String,
        @Query("endDate") endDate: String
    ): Single<PermissionManagementResponse>

    @PUT("/api/v2/permission/management/process/deny/{permissionId}")
    fun putDenyPermission(
        @Path("permissionId") permissionId: Int
    ): Single<ProcessPermissionManagementResponse>

    @PUT("/api/v2/permission/management/process/accept")
    fun putAcceptPermission(
        @Query("permissionId") permissionId: Int,
        @Query("employeeApproveId") employeeApproveId: Int
    ): Single<ProcessPermissionManagementResponse>

    @GET("/api/v2/permission/management/history")
    fun getHistoryPermissionManagement(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int
    ): Single<PermissionsHistoryManagementResponse>

    @GET("/api/v2/permission/management/history/date")
    fun getFilterHistoryManagement(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("page") page: Int
    ): Single<PermissionsHistoryManagementResponse>

    @GET("/api/v2/permission/management/{permissionId}")
    fun getDetailPermissionManagement(
        @Path("permissionId") permissionId: Int
    ): Single<DetailPermissionManagementResponse>

    @Multipart
    @POST("/api/v2/permission/management/upload/image")
    fun uploadPhotoPermission(
        @Query("permissionId") permissionId: Int,
        @Part file: MultipartBody.Part
    ): Single<PermissionManagementResponse>

    @GET("/api/v2/permission/management/check")
    fun getPermissionValidateManagement(
        @Query("adminMasterId") userId: Int
    ): Single<ValidatePermissionManagementResponse>

}