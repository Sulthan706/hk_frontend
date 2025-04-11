package com.hkapps.hygienekleen.features.features_vendor.service.permission.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.service.PermissionService
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CheckCreatePermissionResponse
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CreatePermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.HistoryPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.*
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.historyMid.HistoryPermissionMidResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class PermissionRemoteDataSourceImpl @Inject constructor(private val service: PermissionService) :
    PermissionRemoteDataSource {
    override fun postPermission(
        image: MultipartBody.Part,
        requestBy: Int,
        projectId: String,
        title: String,
        description: String,
        dateStart: String,
        dateEnd: String
    ): Single<CreatePermissionResponseModel> {
        return service. postPermission(image, requestBy, projectId, title, description, dateStart, dateEnd)
    }

    override fun putPermission(
        permissionId: Int,
        employeeApproveId: Int,
        employeeReplaceId: Int,
        projectId: String,
        date: String
    ): Single<PutPermissionResponseModel> {
        return service.putPermission(
            permissionId,
            employeeApproveId,
            employeeReplaceId,
            projectId,
            date
        )
    }

    override fun putDenialPermission(
        permissionId: Int
    ): Single<PutDenialPermissionResponseModel> {
        return service.putDenialPermission(permissionId)
    }

    override fun putPermissionNew(
        permissionId: Int,
        employeeApproveId: Int,
        projectId: String
    ): Single<PutPermissionResponseModel> {
        return service.putPermissionNew(
            permissionId,
            employeeApproveId,
            projectId
        )
    }

    override fun putDenialPermissionNew(permissionId: Int): Single<PutDenialPermissionResponseModel> {
        return service.putDenialPermissionNew(permissionId)
    }

    override fun getHistoryPermission(
        projectId: String,
        employeeId: Int,
        page: Int
    ): Single<HistoryPermissionResponseModel> {
        return service.getHistoryPermission(projectId, employeeId, page)
    }

    override fun getPermission(
        projectId: String,
        employeeId: Int,
        jabatan: String,
        page: Int
    ): Single<PermissionResponseModel> {
        return service.getPermission(projectId, employeeId, jabatan, page)
    }

    override fun getOperatorPermission(
        projectId: String,
        date: String,
        shiftId: Int,
        idDetailEmployeeProject: Int
    ): Single<OperatorPermissionResponseModel> {
        return service.getOperatorPermission(projectId, date, shiftId, idDetailEmployeeProject)
    }

    override fun getDetailPermission(id: Int): Single<DetailPermissionResponse> {
        return service.getDetailPermission(id)
    }

    override fun getCheckPermission(employeeId: Int): Single<CheckCreatePermissionResponse> {
        return service.getCheckPermission(employeeId)
    }

    override fun getHistoryPermissionMid(employeeId: Int): Single<HistoryPermissionMidResponse> {
        return service.getHistoryPermissionMid(employeeId)
    }

    override fun getHistoryDateMid(
        employeeId: Int,
        startDate: String,
        endDate: String
    ): Single<HistoryPermissionMidResponse> {
        return service.getHistoryDateMid(employeeId, startDate, endDate)
    }

    override fun uploadPhotoPermission(
        permissionId: Int,
        file: MultipartBody.Part
    ): Single<DetailPermissionResponse> {
        return service.uploadPhotoPermission(permissionId, file)
    }

}