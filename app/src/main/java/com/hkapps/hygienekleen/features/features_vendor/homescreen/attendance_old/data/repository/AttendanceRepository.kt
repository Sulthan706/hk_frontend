package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface AttendanceRepository {

    fun getChooseStaff(params: String): Single<ChooseStaffResponseModel>

    fun getDailyAct(employeeId: Int, projectCode: String): Single<DailyActResponseModel>

    //GET QR CODE
    fun getQRCode(employeeId: Int, projectCode: String): Single<QRCodeResponseModel>

    //POSTImage
    fun postImageSelfie(employeeId: Int, projectCode: String, barcodeKey: String, imageSelfie: MultipartBody.Part): Single<SelfiePostResponseModel>

    //PUTImage
    fun putImageSelfie(employeeId: Int, projectCode: String, barcodeKey: String, imageSelfie: MultipartBody.Part): Single<SelfiePostResponseModel>

    //GET Status Absen
    fun getStatusAttendance(employeeId: Int, projectCode: String): Single<AttendanceStatusResponseModel>

    //GETHISTORY Absen
    fun getHistory(employeeId: Int, projectCode: String): Single<HistoryResponseModel>
    fun getHistoryByDate(employeeId: Int, projectCode: String, datePreffix: String, dateSuffix: String): Single<HistoryByDateResponseModel>

}