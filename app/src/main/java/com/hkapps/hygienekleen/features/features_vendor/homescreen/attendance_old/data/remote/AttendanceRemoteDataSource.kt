package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.remote


import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface AttendanceRemoteDataSource {
    fun getChooseStaffRDS(params: String): Single<ChooseStaffResponseModel>
    fun getDailyActRDS(employeeId: Int, projectCode: String): Single<DailyActResponseModel>

    //QRCODE
    fun getQRCodeDataRDS(employeeId: Int, projectCode: String): Single<QRCodeResponseModel>
    //POSTImage
    fun postImageSelfieRDS(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel>
    //PUTImage
    fun putImageSelfieRDS(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel>


    fun getHistoryRDS(employeeId: Int, projectCode: String): Single<HistoryResponseModel>
    fun getHistoryByDateRDS(employeeId: Int, projectCode: String, datePreffix: String, dateSuffix: String): Single<HistoryByDateResponseModel>

    //GETAttendance
    fun getAttendanceStatusRDS(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceStatusResponseModel>
}