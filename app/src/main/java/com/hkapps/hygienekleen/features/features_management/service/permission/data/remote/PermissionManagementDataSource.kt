package com.hkapps.hygienekleen.features.features_management.service.permission.data.remote

import com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement.ApprovalPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.createPermission.PermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.detailPermission.DetailPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.historyPermissionManagement.PermissionsHistoryManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.processPermission.ProcessPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.statusCreatePermission.ValidatePermissionManagementResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface PermissionManagementDataSource {

    fun getApprovalPermission (
        userId: Int,
        jabatan: String,
        page: Int,
        size : Int,
    ): Single<ApprovalPermissionManagementResponse>

    fun postPermissionManagement(
        file: MultipartBody.Part,
        requestBy: Int,
        title: String,
        description: String,
        fromDate: String,
        endDate: String
    ): Single<PermissionManagementResponse>

    fun putDenyPermission(
        permissionId: Int
    ): Single<ProcessPermissionManagementResponse>

    fun putAcceptPermission(
        permissionId: Int,
        employeeApproveId: Int
    ): Single<ProcessPermissionManagementResponse>

    fun getHistoryPermissionManagement(
        adminMasterId: Int,
        page: Int
    ): Single<PermissionsHistoryManagementResponse>

    fun getFilterHistoryManagement(
        adminMasterId: Int,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<PermissionsHistoryManagementResponse>

    fun getDetailPermissionManagement(
        permissionId: Int
    ): Single<DetailPermissionManagementResponse>

    fun uploadPhotoPermission(
        permissionId: Int,
        file: MultipartBody.Part
    ): Single<PermissionManagementResponse>

    fun getPermissionValidateManagement(
        userId: Int
    ): Single<ValidatePermissionManagementResponse>

}