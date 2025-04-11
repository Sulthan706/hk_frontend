package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.remote


import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent.AttendanceAlreadyAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.AttendanceNotAbsenResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result.FabHistoryResultResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model.FabPersonResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.FabTeamResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch.FabSearchPersonResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch.MidLevelOneSchResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch.MidLevelOneSchStatusResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.regisfacerecog.RegisFaceRecogResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.userFlyingOperator.AttendanceUserFlyingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.verifyfacerecog.VerifyFaceResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface AttendanceFixRemoteDataSource {
    fun getChooseStaffRDS(params: String): Single<ChooseStaffResponseModel>

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
    ): Single<AttendanceStatusResponseModelNew>

    fun getAttendanceStatusMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchStatusResponseModel>

    fun getAttendanceMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchResponseModel>

    fun getCheckAttendanceRDS(
        id: Int,
        employeeId: Int,
        projectId: String
    ): Single<AttendanceCheckResponseModel>

    fun getCheckAttendanceOutRDS(
        employeeId: Int,
        scheduleId: Int
    ): Single<AttendanceCheckOutResponseModel>

    fun getCheckAttendanceMidOneSchRDS(
        employeeId: Int,
        projectId: String
    ): Single<AttendanceCheckResponseModel>

    fun getCheckAttendanceOutMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceCheckOutResponseModel>

    //Get shift
    fun getShift(
        projectId: String
    ): Single<AttendanceShiftStaffNotAttendanceResponseModel>


    //Get list staff not attendance
    fun getListStaffNotAttendanceRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffNotAttendanceResponseModel>


    fun getListStaffNotAttendanceSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffNotAttendanceSPVResponseModel>


    fun getListStaffNotAttendanceCSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffNotAttendanceCSPVResponseModel>

    fun getListStaffAlreadyAttendanceRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffAlreadyAttendanceResponseModel>


    fun getListStaffAlreadyAttendanceSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffAlreadyAttendanceSPVResponseModel>


    fun getListStaffAlreadyAttendanceCSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffAlreadyAttendanceCSPVResponseModel>

    fun getSchRDS(employeeId: Int, projectCode: String): Single<EmployeeSchResponseModel>
    fun getSchByIdRDS(id: Int, employeeId: Int, projectCode: String): Single<EmployeeSchByIdResponseModel>


    //POSTImageGeo
    fun postImageSelfieGeoRDS(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel>
    //PUTImage
    fun putImageSelfieGeoRDS(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel>


    //Mid
    fun getSchMidRDS(employeeId: Int, projectCode: String): Single<EmployeeSchMidResponseModel>

    //POSTImageGeo
    fun postImageSelfieGeoMidRDS(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel>

    //PUTImage
    fun putImageSelfieGeoMidRDS(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel>

    //get My team
    fun getMyTeam(
        employeeId: Int,
        projectId: String,
        page: Int
    ): Single<FabPersonResponseModel>

    fun getMyTeamSpv(
        projectId: String,
        page: Int
    ): Single<FabPersonResponseModel>

    fun getMyTeamCspv(
        projectId: String,
        page: Int
    ): Single<FabPersonResponseModel>

    fun getHistoryTeamLead(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel>

    fun getHistoryTeamSPV(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel>

    fun getHistoryTeamFM(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel>

    fun getHistoryResult(
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<FabHistoryResultResponseModel>

    fun getSearchPerson(
        projectId: String,
        page: Int,
        keywords: String
    ): Single<FabSearchPersonResponseModel>

    fun getListAttendanceNotAbsent(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceNotAbsenResponseModel>

    fun getListAttendanceAlreadyAbsent(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceAlreadyAbsentResponseModel>

    // attendance user flying
    fun postUserFlyingIn(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        latitude: String,
        longitude: String,
        address: String,
        radius: String,
        description: String,
        deviceInfo: String
    ): Single<AttendanceUserFlyingResponse>

    fun postUserFlyingOut(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        latitude: String,
        longitude: String,
        address: String,
        radius: String,
        description: String,
        deviceInfo: String
    ): Single<AttendanceUserFlyingResponse>

    fun getStatusUserFlying(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        type: String
    ): Single<AttendanceUserFlyingResponse>

    fun getAccessUserFlying(
        projectCode: String
    ): Single<AttendanceUserFlyingResponse>

    fun regisFaceRecog(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<RegisFaceRecogResponseModel>

    fun verifyFaceRecog(
        file: MultipartBody.Part,
        employeeId: Int
    ): Single<VerifyFaceResponseModel>

}