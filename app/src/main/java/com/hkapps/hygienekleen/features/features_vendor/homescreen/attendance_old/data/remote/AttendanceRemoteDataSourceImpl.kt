package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class AttendanceRemoteDataSourceImpl @Inject constructor(private val service: com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.service.AttendanceService) :
    AttendanceRemoteDataSource {
    override fun getChooseStaffRDS(params: String): Single<ChooseStaffResponseModel> {
        return service.getChooseStaffDataApi(params)
    }
    override fun getDailyActRDS(employeeId: Int, projectCode: String): Single<DailyActResponseModel> {
        return service.getDailyDataApi(employeeId, projectCode)
    }

    //QRCode
    override fun getQRCodeDataRDS(employeeId: Int, projectCode: String): Single<QRCodeResponseModel> {
        return service.getQRCodeDataApi(employeeId, projectCode)
    }

    //POSTImage
    override fun postImageSelfieRDS(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel> {
        return service.postImageSelfieApi(employeeId, projectCode, barcodeKey, imageSelfie)
    }


    //PUTImage
    override fun putImageSelfieRDS(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel> {
        return service.putImageSelfieApi(employeeId, projectCode, barcodeKey, imageSelfie)
    }

    //getStatusAbsen
    override fun getAttendanceStatusRDS(employeeId: Int, projectCode: String): Single<AttendanceStatusResponseModel> {
        return service.getAttendanceStatusApi(employeeId, projectCode)
    }


    override fun getHistoryRDS(employeeId: Int, projectCode: String): Single<HistoryResponseModel> {
        return service.getHistoryApi(employeeId, projectCode)
    }
    override fun getHistoryByDateRDS(employeeId: Int, projectCode: String, datePreffix: String, dateSuffix: String): Single<HistoryByDateResponseModel> {
        return service.getHistoryByDateApi(employeeId, projectCode, datePreffix, dateSuffix)
    }
}