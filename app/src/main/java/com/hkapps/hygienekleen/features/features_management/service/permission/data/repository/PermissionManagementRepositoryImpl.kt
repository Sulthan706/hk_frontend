package com.hkapps.hygienekleen.features.features_management.service.permission.data.repository

import com.hkapps.hygienekleen.features.features_management.service.permission.data.remote.PermissionManagementDataSource
import com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement.ApprovalPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.createPermission.PermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.detailPermission.DetailPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.historyPermissionManagement.PermissionsHistoryManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.processPermission.ProcessPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.statusCreatePermission.ValidatePermissionManagementResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class PermissionManagementRepositoryImpl @Inject constructor(private val dataSource: PermissionManagementDataSource): PermissionManagementRepository {

    override fun getApprovalPermission(
        userId: Int,
        jabatan: String,
        page: Int,
        size : Int,
    ): Single<ApprovalPermissionManagementResponse> {
        return dataSource.getApprovalPermission(userId, jabatan, page,size)
    }

    override fun postPermissionManagement(
        file: MultipartBody.Part,
        requestBy: Int,
        title: String,
        description: String,
        fromDate: String,
        endDate: String
    ): Single<PermissionManagementResponse> {
        return dataSource.postPermissionManagement(file, requestBy, title, description, fromDate, endDate)
    }

    override fun putDenyPermission(permissionId: Int): Single<ProcessPermissionManagementResponse> {
        return dataSource.putDenyPermission(permissionId)
    }

    override fun putAcceptPermission(
        permissionId: Int,
        employeeApproveId: Int
    ): Single<ProcessPermissionManagementResponse> {
        return dataSource.putAcceptPermission(permissionId, employeeApproveId)
    }

    override fun getHistoryPermissionManagement(
        adminMasterId: Int,
        page: Int
    ): Single<PermissionsHistoryManagementResponse> {
        return dataSource.getHistoryPermissionManagement(adminMasterId, page)
    }

    override fun getFilterHistoryManagement(
        adminMasterId: Int,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<PermissionsHistoryManagementResponse> {
        return dataSource.getFilterHistoryManagement(adminMasterId, startDate, endDate, page)
    }

    override fun getDetailPermissionManagement(permissionId: Int): Single<DetailPermissionManagementResponse> {
        return dataSource.getDetailPermissionManagement(permissionId)
    }

    override fun uploadPhotoPermission(
        permissionId: Int,
        file: MultipartBody.Part
    ): Single<PermissionManagementResponse> {
        return dataSource.uploadPhotoPermission(permissionId, file)
    }

    override fun getPermissionValidateManagement(userId: Int): Single<ValidatePermissionManagementResponse> {
        return dataSource.getPermissionValidateManagement(userId)
    }

}