package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.service.AttendanceFixService
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

class AttendanceFixRemoteDataSourceImpl @Inject constructor(private val service: AttendanceFixService) :
    AttendanceFixRemoteDataSource {
    override fun getChooseStaffRDS(params: String): Single<ChooseStaffResponseModel> {
        return service.getChooseStaffDataApi(params)
    }

    //QRCode
    override fun getQRCodeDataRDS(
        employeeId: Int,
        projectCode: String
    ): Single<QRCodeResponseModel> {
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
    override fun getAttendanceStatusRDS(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceStatusResponseModelNew> {
        return service.getAttendanceStatusOperatorApi(employeeId, projectCode)
    }

    override fun getAttendanceStatusMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchStatusResponseModel> {
        return service.getAttendanceStatusMidOneSchApi(employeeId, projectCode)
    }

    override fun getAttendanceMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<MidLevelOneSchResponseModel> {
        return service.getAttendanceMidOneSchApi(employeeId, projectCode)
    }

    override fun getCheckAttendanceRDS(
        id: Int,
        employeeId: Int,
        projectId: String
    ): Single<AttendanceCheckResponseModel> {
        return service.getCheckAttendanceApi(id, employeeId, projectId)
    }

    override fun getCheckAttendanceOutRDS(
        employeeId: Int,
        scheduleId: Int
    ): Single<AttendanceCheckOutResponseModel> {
        return service.getCheckAttendanceOutApi(employeeId, scheduleId)
    }

    override fun getCheckAttendanceMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceCheckResponseModel> {
        return service.getCheckAttendanceMidOneSchApi(employeeId, projectCode)
    }

    override fun getCheckAttendanceOutMidOneSchRDS(
        employeeId: Int,
        projectCode: String
    ): Single<AttendanceCheckOutResponseModel> {
        return service.getCheckAttendanceOutMidOneSchApi(employeeId, projectCode)
    }

    override fun getHistoryRDS(employeeId: Int, projectCode: String): Single<HistoryResponseModel> {
        return service.getHistoryApi(employeeId, projectCode)
    }

    override fun getHistoryByDateRDS(
        employeeId: Int,
        projectCode: String,
        datePreffix: String,
        dateSuffix: String
    ): Single<HistoryByDateResponseModel> {
        return service.getHistoryByDateApi(employeeId, projectCode, datePreffix, dateSuffix)
    }

    //Get shift
    override fun getShift(projectId: String): Single<AttendanceShiftStaffNotAttendanceResponseModel> {
        return service.getShift(projectId)
    }

    //Get list staff not attendance
    override fun getListStaffNotAttendanceRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceResponseModel> {
        return service.getListStaffNotAttendance(projectCode, employeeId, shiftId)
    }

    override fun getListStaffNotAttendanceSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceSPVResponseModel> {
        return service.getListStaffNotAttendanceSPV(projectCode, employeeId, shiftId)
    }

    override fun getListStaffNotAttendanceCSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceCSPVResponseModel> {
        return service.getListStaffNotAttendanceCSPV(projectCode, employeeId, shiftId)
    }

    override fun getListStaffAlreadyAttendanceRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int,
    ): Single<AttendanceListStaffAlreadyAttendanceResponseModel> {
        return service.getListStaffAlreadyAttendance(projectCode, employeeId, shiftId)
    }

    override fun getListStaffAlreadyAttendanceSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceSPVResponseModel> {
        return service.getListStaffAlreadyAttendanceSPV(projectCode, employeeId, shiftId)
    }

    override fun getListStaffAlreadyAttendanceCSPVRDS(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceCSPVResponseModel> {
        return service.getListStaffAlreadyAttendanceCSPV(projectCode, employeeId, shiftId)
    }

    override fun getSchRDS(employeeId: Int, projectCode: String): Single<EmployeeSchResponseModel> {
        return service.getEmployeeSchApi(employeeId, projectCode)
    }

    override fun getSchByIdRDS(id: Int, employeeId: Int, projectCode: String): Single<EmployeeSchByIdResponseModel> {
        return service.getEmployeeSchByIdApi(id, employeeId, projectCode)
    }

    //POSTImageGeo
    override fun postImageSelfieGeoRDS(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel> {
        return service.postImageSelfieGeoApi(employeeId, projectCode, scheduleId, imageSelfie)
    }

    //PUTImageGeo
    override fun putImageSelfieGeoRDS(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostResponseModel> {
        return service.putImageSelfieGeoApi(employeeId, projectCode, scheduleId, imageSelfie)
    }


    //MID
    override fun getSchMidRDS(
        employeeId: Int,
        projectCode: String
    ): Single<EmployeeSchMidResponseModel> {
        return service.getEmployeeSchMidApi(employeeId, projectCode)
    }

    override fun postImageSelfieGeoMidRDS(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel> {
        return service.postImageSelfieGeoMidApi(employeeId, projectCode, imageSelfie)
    }

    override fun putImageSelfieGeoMidRDS(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ): Single<GeoSelfiePostMidResponseModel> {
        return service.putImageSelfieGeoMidApi(employeeId, projectCode, imageSelfie)
    }

    override fun getMyTeam(
        employeeId: Int,
        projectId: String,
        page: Int
    ): Single<FabPersonResponseModel> {
        return service.getMyTeam(projectId,employeeId,page)
    }

    override fun getMyTeamSpv(projectId: String, page: Int): Single<FabPersonResponseModel> {
        return service.getMyTeamSpv(projectId, page)
    }

    override fun getMyTeamCspv(projectId: String, page: Int): Single<FabPersonResponseModel> {
        return service.getMyTeamCspv(projectId, page)
    }

    override fun getHistoryTeamLead(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel> {
       return service.getHistoryTeamLeader(employeeId, projectCode, date, shiftId)
    }

    override fun getHistoryTeamSPV(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel> {
        return service.getHistoryTeamSPV(employeeId, projectCode, date, shiftId)
    }

    override fun getHistoryTeamFM(
        employeeId: Int,
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<FabTeamResponseModel> {
        return service.getHistoryTeamFM(employeeId, projectCode, date, shiftId)
    }

    override fun getHistoryResult(
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<FabHistoryResultResponseModel> {
        return service.getHistoryResult(employeeId, month, year)
    }

    override fun getSearchPerson(
        projectId: String,
        page: Int,
        keywords: String
    ): Single<FabSearchPersonResponseModel> {
        return service.getSearchPerson(projectId, page, keywords)
    }

    override fun getListAttendanceNotAbsent(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceNotAbsenResponseModel> {
        return service.getListAttendanceNotAbsent(employeeId, projectCode, shiftId)
    }

    override fun getListAttendanceAlreadyAbsent(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<AttendanceAlreadyAbsentResponseModel> {
        return service.getListAttendanceAlreadyAbsent(employeeId, projectCode, shiftId)
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
        return service.postUserFlyingIn(userId, idSchedule, projectCode, latitude, longitude, address, radius, description, deviceInfo)
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
        return service.postUserFlyingOut(userId, idSchedule, projectCode, latitude, longitude, address, radius, description, deviceInfo)
    }

    override fun getStatusUserFlying(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        type: String
    ): Single<AttendanceUserFlyingResponse> {
        return service.getStatusUserFlying(userId, idSchedule, projectCode, type)
    }

    override fun getAccessUserFlying(projectCode: String): Single<AttendanceUserFlyingResponse> {
        return service.getAccessUserFlying(projectCode)
    }

    override fun regisFaceRecog(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<RegisFaceRecogResponseModel> {
        return service.regisFaceRecog(employeeId, file)
    }

    override fun verifyFaceRecog(file: MultipartBody.Part, employeeId: Int): Single<VerifyFaceResponseModel> {
        return service.verifyFaceRecog(file, employeeId)
    }

}