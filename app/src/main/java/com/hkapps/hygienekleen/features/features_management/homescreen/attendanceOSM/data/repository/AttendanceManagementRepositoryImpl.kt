package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.remote.AttendanceManagementDataSource
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

class AttendanceManagementRepositoryImpl @Inject constructor(private val dataSource: AttendanceManagementDataSource) :
    AttendanceManagementRepository {

    override fun attendanceInGeo(
        userId: Int,
        projectCode: String,
        jabatan: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse> {
        return dataSource.attendanceInGeo(userId, projectCode, jabatan, file, longitude, latitude, address)
    }

    override fun attendanceOutGeo(
        userId: Int,
        projectCode: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse> {
        return dataSource.attendanceOutGeo(userId, projectCode, file, longitude, latitude, address)
    }

    override fun getStatusAttendance(
        userId: Int,
        projectCode: String
    ): Single<AttendanceStatusManagementResponse> {
        return dataSource.getStatusAttendance(userId, projectCode)
    }

    override fun getProjectsManagement(
        userId: Int,
        page: Int,
        size: Int
    ): Single<ProjectsAllAttendanceResponse> {
        return dataSource.getProjectsManagement(userId, page,size)
    }

    override fun getAllProject(page: Int, size: Int): Single<ProjectsAllAttendanceResponse> {
        return dataSource.getAllProject(page, size)
    }

    override fun getSearchProjectAll(
        page: Int,
        keywords: String
    ): Single<ProjectsAllAttendanceResponse> {
        return dataSource.getSearchProjectAll(page, keywords)
    }

    override fun getSearchProjectManagement(
        userId: Int,
        page: Int,
        keywords: String
    ): Single<ProjectsAllAttendanceResponse> {
        return dataSource.getSearchProjectManagement(userId, page, keywords)
    }

    override fun getSchedulesAttendance(
        adminMasterId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ): Single<SchedulesAttendanceManagementModel> {
        return dataSource.getSchedulesAttendance(adminMasterId, date, type, page, size)
    }

    override fun getAttendanceStatusV2(
        userId: Int,
        idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse> {
        return dataSource.getAttendanceStatusV2(userId, idRkbOperation)
    }

    override fun attendanceInGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoManagementResponse> {
        return dataSource.attendanceInGeoV2(userId, idRkbOperation, file, longitude, latitude, address)
    }

    override fun attendanceOutGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoManagementResponse> {
        return dataSource.attendanceOutGeoV2(userId, idRkbOperation, file, longitude, latitude, address)
    }

    override fun submitExtendVisitDuration(
        userId: Int,
        idRkb: Int,
        duration: Int,
        extendReason: String
    ): Single<ExtendDurationVisitResponse> {
        return dataSource.submitExtendVisitDuration(userId, idRkb, duration, extendReason)
    }

    override fun getDetailScheduleManagement(
        idRkb: Int,
        userId: Int
    ): Single<DetailScheduleManagementResponse> {
        return dataSource.getDetailScheduleManagement(idRkb, userId)
    }

    override fun attendanceInGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoBodResponse> {
        return dataSource.attendanceInGeoBodV2(userId, idRkbBod, file, longitude, latitude, address)
    }

    override fun getAttendanceStatusBod(
        userId: Int,
        idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse> {
        return dataSource.getAttendanceStatusBod(userId, idRkbOperation)
    }

    override fun attendanceOutGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address: String
    ): Single<AttendanceGeoBodResponse> {
        return dataSource.attendanceOutGeoBodV2(userId, idRkbBod, file, longitude, latitude, address)
    }

    override fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ProjectsAllAttendanceResponse> {
        return dataSource.getSearchProjectBranch(page, branchCode, keywords, perPage)
    }

}