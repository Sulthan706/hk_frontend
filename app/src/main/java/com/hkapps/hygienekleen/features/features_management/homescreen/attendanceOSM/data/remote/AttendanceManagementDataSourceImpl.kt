package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.service.AttendanceManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceCheckInOut.AttendanceGeoManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceInOutBod.AttendanceGeoBodResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceStatus.AttendanceStatusManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.detailSchedule.DetailScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.extendVisitDuration.ExtendDurationVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.ProjectsAllAttendanceResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement.SchedulesAttendanceManagementModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class AttendanceManagementDataSourceImpl @Inject constructor(private val service: AttendanceManagementService) :
    AttendanceManagementDataSource {

    override fun attendanceInGeo(
        userId: Int,
        projectCode: String,
        jabatan: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse> {
        return service.attendanceInGeo(userId, projectCode, jabatan, file, longitude, latitude, address)
    }

    override fun attendanceOutGeo(
        userId: Int,
        projectCode: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse> {
        return service.attendanceOutGeo(userId, projectCode, file, longitude, latitude, address)
    }

    override fun getStatusAttendance(
        userId: Int,
        projectCode: String
    ): Single<AttendanceStatusManagementResponse> {
        return service.getStatusAttendance(userId, projectCode)
    }

    override fun getProjectsManagement(
        userId: Int,
        page: Int,
        size: Int
    ): Single<ProjectsAllAttendanceResponse> {
        return service.getProjectsManagement(userId, page,size)
    }

    override fun getAllProject(page: Int, size: Int): Single<ProjectsAllAttendanceResponse> {
        return service.getAllProject(page, size)
    }

    override fun getSearchProjectAll(
        page: Int,
        keywords: String
    ): Single<ProjectsAllAttendanceResponse> {
        return service.getSearchProjectAll(page, keywords)
    }

    override fun getSearchProjectManagement(
        userId: Int,
        page: Int,
        keywords: String
    ): Single<ProjectsAllAttendanceResponse> {
        return service.getSearchProjectManagement(userId, page, keywords)
    }

    override fun getSchedulesAttendance(
        adminMasterId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ): Single<SchedulesAttendanceManagementModel> {
        return service.getSchedulesAttendance(adminMasterId, date, type, page, size)
    }

    override fun getAttendanceStatusV2(
        userId: Int,
        idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse> {
        return service.getAttendanceStatusV2(userId, idRkbOperation)
    }

    override fun attendanceInGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoManagementResponse> {
        return service.attendanceInGeoV2(userId, idRkbOperation, file, longitude, latitude, address)
    }

    override fun attendanceOutGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoManagementResponse> {
        return service.attendanceOutGeoV2(userId, idRkbOperation, file, longitude, latitude, address)
    }

    override fun submitExtendVisitDuration(
        userId: Int,
        idRkb: Int,
        duration: Int,
        extendReason: String
    ): Single<ExtendDurationVisitResponse> {
        return service.submitExtendVisitDuration(userId, idRkb, duration, extendReason)
    }

    override fun getDetailScheduleManagement(
        idRkb: Int,
        userId: Int
    ): Single<DetailScheduleManagementResponse> {
        return service.getDetailScheduleManagement(idRkb, userId)
    }

    override fun attendanceInGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoBodResponse> {
        return service.attendanceInGeoBodV2(userId, idRkbBod, file, longitude, latitude, address)
    }

    override fun getAttendanceStatusBod(
        userId: Int,
        idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse> {
        return service.getAttendanceStatusBod(userId, idRkbOperation)
    }

    override fun attendanceOutGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoBodResponse> {
        return service.attendanceOutGeoBodV2(userId, idRkbBod, file, longitude, latitude, address)
    }

    override fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ProjectsAllAttendanceResponse> {
        return service.getSearchProjectBranch(page, branchCode, keywords, perPage)
    }

}