package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(private val remoteDataSource: com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.remote.AttendanceRemoteDataSource) :
    AttendanceRepository {

    override fun getChooseStaff(params: String): Single<ChooseStaffResponseModel> {
        return remoteDataSource.getChooseStaffRDS(params)
    }
    override fun getDailyAct(employeeId: Int, projectCode: String): Single<DailyActResponseModel> {
        return remoteDataSource.getDailyActRDS(employeeId, projectCode)
    }

    //getQR
    override fun getQRCode(employeeId: Int, projectCode: String): Single<QRCodeResponseModel> {
        return remoteDataSource.getQRCodeDataRDS(employeeId, projectCode)
    }

    //POSTImage
    override fun postImageSelfie(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel> {
        return remoteDataSource.postImageSelfieRDS(employeeId, projectCode, barcodeKey, imageSelfie)
    }

    //PUTImage
    override fun putImageSelfie(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel> {
        return remoteDataSource.putImageSelfieRDS(employeeId, projectCode, barcodeKey, imageSelfie)
    }

    //getStatusAbse
    override fun getStatusAttendance(employeeId: Int, projectCode: String): Single<AttendanceStatusResponseModel> {
        return remoteDataSource.getAttendanceStatusRDS(employeeId, projectCode)
    }

    //getHistoryabsen
    override fun getHistory(employeeId: Int, projectCode: String): Single<HistoryResponseModel> {
        return remoteDataSource.getHistoryRDS(employeeId, projectCode)
    }

    override fun getHistoryByDate(employeeId: Int, projectCode: String, datePreffix: String, dateSuffix: String): Single<HistoryByDateResponseModel> {
        return remoteDataSource.getHistoryByDateRDS(employeeId, projectCode, datePreffix, dateSuffix)
    }
}