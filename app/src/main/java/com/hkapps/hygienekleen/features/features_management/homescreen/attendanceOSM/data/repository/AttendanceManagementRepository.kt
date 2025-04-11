package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceCheckInOut.AttendanceGeoManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceInOutBod.AttendanceGeoBodResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceStatus.AttendanceStatusManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.detailSchedule.DetailScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.extendVisitDuration.ExtendDurationVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.ProjectsAllAttendanceResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement.SchedulesAttendanceManagementModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface AttendanceManagementRepository {

    fun attendanceInGeo(
        userId: Int,
        projectCode: String,
        jabatan: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse>

    fun attendanceOutGeo(
        userId: Int,
        projectCode: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse>

    fun getStatusAttendance(
        userId: Int,
        projectCode: String
    ): Single<AttendanceStatusManagementResponse>

    fun getProjectsManagement(
        userId: Int,
        page: Int,
        size: Int
    ): Single<ProjectsAllAttendanceResponse>

    fun getAllProject(
        page: Int,
        size: Int
    ): Single<ProjectsAllAttendanceResponse>

    fun getSearchProjectAll(
        page: Int,
        keywords: String
    ): Single<ProjectsAllAttendanceResponse>

    fun getSearchProjectManagement(
        userId: Int,
        page: Int,
        keywords: String
    ): Single<ProjectsAllAttendanceResponse>

    fun getSchedulesAttendance(
        adminMasterId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ): Single<SchedulesAttendanceManagementModel>

    fun getAttendanceStatusV2(
        userId: Int,
        idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse>

    fun attendanceInGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse>

    fun attendanceOutGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoManagementResponse>

    fun submitExtendVisitDuration(
        userId: Int,
        idRkb: Int,
        duration: Int,
        extendReason: String
    ): Single<ExtendDurationVisitResponse>

    fun getDetailScheduleManagement(
        idRkb: Int,
        userId: Int
    ): Single<DetailScheduleManagementResponse>

    fun attendanceInGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoBodResponse>

    fun getAttendanceStatusBod(
        userId: Int,
        idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse>

    fun attendanceOutGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ): Single<AttendanceGeoBodResponse>

    fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ProjectsAllAttendanceResponse>

}