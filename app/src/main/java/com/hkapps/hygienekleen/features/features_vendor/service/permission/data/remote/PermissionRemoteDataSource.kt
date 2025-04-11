package com.hkapps.hygienekleen.features.features_vendor.service.permission.data.remote
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CheckCreatePermissionResponse
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CreatePermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.HistoryPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.*
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.historyMid.HistoryPermissionMidResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface PermissionRemoteDataSource {

    fun postPermission(
        image: MultipartBody.Part,
        requestBy: Int,
        projectId: String,
        title: String,
        description: String,
        dateStart: String,
        dateEnd: String
    ): Single<CreatePermissionResponseModel>

    fun putPermission(
        permissionId: Int,
        employeeApproveId: Int,
        employeeReplaceId: Int,
        projectId: String,
        date: String
    ): Single<PutPermissionResponseModel>

    fun putDenialPermission(permissionId: Int): Single<PutDenialPermissionResponseModel>

    fun putPermissionNew(
        permissionId: Int,
        employeeApproveId: Int,
        projectId: String
    ): Single<PutPermissionResponseModel>

    fun putDenialPermissionNew(permissionId: Int): Single<PutDenialPermissionResponseModel>

    fun getHistoryPermission(
        projectId: String,
        employeeId: Int,
        page: Int
    ): Single<HistoryPermissionResponseModel>

    fun getPermission(
        projectId: String,
        employeeId: Int,
        jabatan: String,
        page: Int
    ): Single<PermissionResponseModel>

    fun getOperatorPermission(
        projectId: String,
        date: String,
        shiftId: Int,
        idDetailEmployeeProject: Int
    ): Single<OperatorPermissionResponseModel>

    fun getDetailPermission(id: Int): Single<DetailPermissionResponse>

    fun getCheckPermission(
        employeeId: Int
    ): Single<CheckCreatePermissionResponse>

    fun getHistoryPermissionMid(
        employeeId: Int
    ): Single<HistoryPermissionMidResponse>

    fun getHistoryDateMid(
        employeeId: Int,
        startDate: String,
        endDate: String
    ): Single<HistoryPermissionMidResponse>

    fun uploadPhotoPermission(
        permissionId: Int,
        file: MultipartBody.Part
    ): Single<DetailPermissionResponse>

}