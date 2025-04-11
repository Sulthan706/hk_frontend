package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.remote.AttendanceFixRemoteDataSource
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
import javax.inject.Inject

class AttendanceFixRepositoryImpl @Inject constructor(private val remoteDataSource: AttendanceFixRemoteDataSource) :
    AttendanceFixRepository {

    override fun getChooseStaff(params: String): Single<ChooseStaffResponseModel> {
        return remoteDataSource.getChooseStaffRDS(params)
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


    //POSTImage Geo
    override fun postImageSelfieGeo(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel> {
        return remoteDataSource.postImageSelfieGeoRDS(
            employeeId,
            projectCode,
            scheduleId,
            imageSelfie
        )
    }

    //PUTImage Geo
    override fun putImageSelfieGeo(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel> {
        return remoteDataSource.putImageSelfieGeoRDS(
            employeeId,
            projectCode,
            scheduleId,
            imageSelfie
        )
    }

    //getStatusAbse
    override fun getStatusAttendance(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceStatusResponseModelNew> {
        return remoteDataSource.getAttendanceStatusRDS(employeeId, projectCode)
    }

    override fun getStatusAttendanceMidOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchStatusResponseModel> {
        return remoteDataSource.getAttendanceStatusMidOneSchRDS(employeeId, projectCode)
    }

    override fun getAttendanceMidOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchResponseModel> {
        return remoteDataSource.getAttendanceMidOneSchRDS(employeeId, projectCode)
    }

    override fun getCheckAttendance(
        id: Int,
        employeeId: Int,
        projectId: String
    ): Single<AttendanceCheckResponseModel> {
        return remoteDataSource.getCheckAttendanceRDS(id, employeeId, projectId)
    }

    override fun getCheckAttendanceOut(
        employeeId: Int,
        scheduleId: Int
    ): Single<AttendanceCheckOutResponseModel> {
        return remoteDataSource.getCheckAttendanceOutRDS(employeeId, scheduleId)
    }

    override fun getCheckAttendanceMidLevelOneSch(
        employeeId: Int,
        projectId: String
    ): Single<AttendanceCheckResponseModel> {
        return remoteDataSource.getCheckAttendanceMidOneSchRDS(employeeId, projectId)
    }

    override fun getCheckAttendanceOutMidLevelOneSch(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceCheckOutResponseModel> {
        return remoteDataSource.getCheckAttendanceOutMidOneSchRDS(employeeId, projectCode)
    }

    //getHistoryabsen
    override fun getHistory(employeeId: Int, projectCode: String): Single<HistoryResponseModel> {
        return remoteDataSource.getHistoryRDS(employeeId, projectCode)
    }

    override fun getHistoryByDate(
        employeeId: Int,
        projectCode: String,
        datePreffix: String,
        dateSuffix: String
    ): Single<HistoryByDateResponseModel> {
        return remoteDataSource.getHistoryByDateRDS(
            employeeId,
            projectCode,
            datePreffix,
            dateSuffix
        )
    }

    //Get shift
    override fun getShift(projectId: String): Single<AttendanceShiftStaffNotAttendanceResponseModel> {
        return remoteDataSource.getShift(projectId)
    }

    //Get list staff not attendance new
    override fun getListStaffNotAttendance(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceNotAbsenResponseModel> {
        return remoteDataSource.getListAttendanceNotAbsent(employeeId, projectCode, shiftId)
    }

    override fun getListStaffNotAttendanceSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceSPVResponseModel> {
        return remoteDataSource.getListStaffNotAttendanceSPVRDS(projectCode, employeeId, shiftId)
    }

    override fun getListStaffNotAttendanceCSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceCSPVResponseModel> {
        return remoteDataSource.getListStaffNotAttendanceCSPVRDS(projectCode, employeeId, shiftId)
    }

    override fun getListStaffAlreadyAttendance(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceResponseModel> {
        return remoteDataSource.getListStaffAlreadyAttendanceRDS(projectCode, employeeId, shiftId)
    }

    override fun getListStaffAlreadyAttendanceSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceSPVResponseModel> {
        return remoteDataSource.getListStaffAlreadyAttendanceSPVRDS(projectCode, employeeId, shiftId)
    }

    override fun getListStaffAlreadyAttendanceCSPV(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceCSPVResponseModel> {
        return remoteDataSource.getListStaffAlreadyAttendanceCSPVRDS(projectCode, employeeId, shiftId)
    }

    override fun getSch(employeeId: Int, projectCode: String): Single<EmployeeSchResponseModel> {
        return remoteDataSource.getSchRDS(employeeId, projectCode)
    }

    override fun getSchById(
        id: Int,
        employeeId: Int,
        projectCode: String
    ): Single<EmployeeSchByIdResponseModel> {
        return remoteDataSource.getSchByIdRDS(id, employeeId, projectCode)
    }


    //MID
    override fun postImageSelfieGeoMid(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel> {
        return remoteDataSource.postImageSelfieGeoMidRDS(employeeId, projectCode, imageSelfie)
    }

    override fun putImageSelfieGeoMid(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel> {
        return remoteDataSource.putImageSelfieGeoMidRDS(employeeId, projectCode, imageSelfie)
    }

    override fun getSchMid(
        employeeId: Int,
        projectCode: String
    ): Single<EmployeeSchMidResponseModel> {
        return remoteDataSource.getSchMidRDS(employeeId, projectCode)
    }

    override fun getMyTeam(employeeId: Int, projectId: String, page: Int): Single<FabPersonResponseModel> {
        return remoteDataSource.getMyTeam(employeeId,projectId,page)
    }

    override fun getMyTeamSpv(projectId: String, page: Int): Single<FabPersonResponseModel> {
        return remoteDataSource.getMyTeamSpv(projectId, page)
    }

    override fun getMyTeamCspv(projectId: String, page: Int): Single<FabPersonResponseModel> {
        return remoteDataSource.getMyTeamCspv(projectId, page)
    }

    override fun getHistoryTeamLead(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel> {
        return remoteDataSource.getHistoryTeamLead(employeeId, projectCode, date, shiftId)
    }

    override fun getHistorySPV(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel> {
        return remoteDataSource.getHistoryTeamSPV(employeeId, projectCode, date, shiftId)
    }

    override fun getHistoryFM(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel> {
        return remoteDataSource.getHistoryTeamFM(employeeId,projectCode, date, shiftId)
    }

    override fun getHistoryResult(
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<FabHistoryResultResponseModel> {
        return remoteDataSource.getHistoryResult(employeeId, month, year)
    }

    override fun getSearchPerson(
        projectId: String,
        page: Int,
        keywords: String
    ): Single<FabSearchPersonResponseModel> {
        return remoteDataSource.getSearchPerson(projectId, page, keywords)
    }

    override fun getListAttendanceNotAbsent(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceNotAbsenResponseModel> {
        return remoteDataSource.getListAttendanceNotAbsent(employeeId, projectCode, shiftId)
    }

    override fun getListAttendanceAlreadyAbsent(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceAlreadyAbsentResponseModel> {
        return remoteDataSource.getListAttendanceAlreadyAbsent(employeeId,  projectCode, shiftId)
    }

    override fun postUserFlyingIn(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        latitude: String,
        longitude: String,
        address: String,
        radius: String,
        description: String,
        deviceInfo: String
    ): Single<AttendanceUserFlyingResponse> {
        return remoteDataSource.postUserFlyingIn(userId, idSchedule, projectCode, latitude, longitude, address, radius, description, deviceInfo)
    }

    override fun postUserFlyingOut(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        latitude: String,
        longitude: String,
        address: String,
        radius: String,
        description: String,
        deviceInfo: String
    ): Single<AttendanceUserFlyingResponse> {
        return remoteDataSource.postUserFlyingOut(userId, idSchedule, projectCode, latitude, longitude, address, radius, description, deviceInfo)
    }

    override fun getStatusUserFlying(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        type: String
    ): Single<AttendanceUserFlyingResponse> {
        return remoteDataSource.getStatusUserFlying(userId, idSchedule, projectCode, type)
    }

    override fun getAccessUserFlying(projectCode: String): Single<AttendanceUserFlyingResponse> {
        return remoteDataSource.getAccessUserFlying(projectCode)
    }

    override fun regisFaceRecog(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<RegisFaceRecogResponseModel> {
        return remoteDataSource.regisFaceRecog(employeeId, file)
    }

    override fun verifyFaceRecog(file: MultipartBody.Part, employeeId: Int): Single<VerifyFaceResponseModel> {
        return remoteDataSource.verifyFaceRecog(file, employeeId)
    }

}