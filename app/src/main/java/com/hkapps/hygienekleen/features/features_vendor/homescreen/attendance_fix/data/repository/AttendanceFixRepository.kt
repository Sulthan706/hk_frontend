package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.repository

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

interface AttendanceFixRepository {

    fun getChooseStaff(params: String): Single<ChooseStaffResponseModel>

    //GET QR CODE
    fun getQRCode(employeeId: Int, projectCode: String): Single<QRCodeResponseModel>

    //POSTImage
    fun postImageSelfie(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel>

    fun postImageSelfieGeo(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel>

    //PUTImage
    fun putImageSelfie(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ): Single<SelfiePostResponseModel>

    fun putImageSelfieGeo(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel>

    //GET Status Absen
    fun getStatusAttendance(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceStatusResponseModelNew>

    fun getStatusAttendanceMidOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchStatusResponseModel>

    fun getAttendanceMidOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchResponseModel>

    fun getCheckAttendance(
        id: Int,
        employeeId: Int,
        projectId: String
    ): Single<AttendanceCheckResponseModel>

    fun getCheckAttendanceOut(
        employeeId: Int,
        scheduleId: Int
    ): Single<AttendanceCheckOutResponseModel>

    fun getCheckAttendanceMidLevelOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceCheckResponseModel>

    fun getCheckAttendanceOutMidLevelOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceCheckOutResponseModel>

    //GETHISTORY Absen
    fun getHistory(employeeId: Int, projectCode: String): Single<HistoryResponseModel>
    fun getHistoryByDate(
        employeeId: Int,
        projectCode: String,
        datePreffix: String,
        dateSuffix: String
    ): Single<HistoryByDateResponseModel>


    //GET shift
    fun getShift(projectId: String): Single<AttendanceShiftStaffNotAttendanceResponseModel>

    //GET List Staff Not attendance
    fun getListStaffNotAttendance(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceNotAbsenResponseModel>


    //GET List Staff Not attendance
    fun getListStaffNotAttendanceSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceSPVResponseModel>


    //GET List Staff Not attendance
    fun getListStaffNotAttendanceCSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceCSPVResponseModel>

    //GET List Staff Already attendance
    fun getListStaffAlreadyAttendance(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceResponseModel>


    fun getListStaffAlreadyAttendanceSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceSPVResponseModel>

    fun getListStaffAlreadyAttendanceCSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceCSPVResponseModel>


    //GET SCHEDULE IN MAPS
    fun getSch(employeeId: Int, projectCode: String): Single<EmployeeSchResponseModel>
    fun getSchById(
        id: Int,
        employeeId: Int,
        projectCode: String
    ): Single<EmployeeSchByIdResponseModel>


    //MID
    fun postImageSelfieGeoMid(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel>

    fun putImageSelfieGeoMid(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel>

    fun getSchMid(employeeId: Int, projectCode: String): Single<EmployeeSchMidResponseModel>

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

    fun getHistorySPV(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel>

    fun getHistoryFM(
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