package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceCheckInOut.AttendanceGeoManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceInOutBod.AttendanceGeoBodResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceStatus.AttendanceStatusManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.detailSchedule.DetailScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.extendVisitDuration.ExtendDurationVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.ProjectsAllAttendanceResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement.SchedulesAttendanceManagementModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface AttendanceManagementService {

    @Multipart
    @PUT("/api/v1/employee/management/attendance/in/geo")
    fun attendanceInGeo(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("jabatan") jabatan: String,
        @Part file: MultipartBody.Part,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("address") address :String
    ): Single<AttendanceGeoManagementResponse>

    @Multipart
    @PUT("/api/v1/employee/management/attendance/out/geo")
    fun attendanceOutGeo(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Part file: MultipartBody.Part,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("address") address :String
    ): Single<AttendanceGeoManagementResponse>

    @GET("/api/v1/employee/management/attendance/check/status")
    fun getStatusAttendance(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String
    ): Single<AttendanceStatusManagementResponse>

    @GET("/api/v1/employee/management/project/{adminMasterId}")
    fun getProjectsManagement(
        @Path("adminMasterId") userId: Int,
        @Query("page") page: Int,
        @Query("size") size : Int
    ): Single<ProjectsAllAttendanceResponse>

    @GET("/api/v1/project")
    fun getAllProject(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Single<ProjectsAllAttendanceResponse>

    @GET("/api/v1/project/search/all")
    fun getSearchProjectAll(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ProjectsAllAttendanceResponse>

    @GET("/api/v1/project/search/own/{adminMasterId}")
    fun getSearchProjectManagement(
        @Path("adminMasterId") userId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ProjectsAllAttendanceResponse>

    @GET("/api/v2/employee/management/get/schedules")
    fun getSchedulesAttendance(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("date") date: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<SchedulesAttendanceManagementModel>

    @GET("api/v2/employee/management/get/status")
    fun getAttendanceStatusV2(
        @Query("adminMasterId") userId: Int,
        @Query("idRkbOperation") idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse>

    @Multipart
    @POST("api/v2/employee/management/post/attendance-in")
    fun attendanceInGeoV2(
        @Query("adminMasterId") userId: Int,
        @Query("idRkbOperation") idRkbOperation: Int,
        @Part file: MultipartBody.Part,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("address") address :String
    ): Single<AttendanceGeoManagementResponse>

    @Multipart
    @PATCH("api/v2/employee/management/patch/attendance-out")
    fun attendanceOutGeoV2(
        @Query("adminMasterId") userId: Int,
        @Query("idRkbOperation") idRkbOperation: Int,
        @Part file: MultipartBody.Part,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("address") address :String
    ): Single<AttendanceGeoManagementResponse>

    @PATCH("api/v2/employee/management/patch/extend-visit-duration")
    fun submitExtendVisitDuration(
        @Query("adminMasterId") userId: Int,
        @Query("idRkb") idRkb: Int,
        @Query("duration") duration: Int,
        @Query("extendReason") extendReason: String
    ): Single<ExtendDurationVisitResponse>

    @GET("api/v2/employee/management/get/detail/schedules")
    fun getDetailScheduleManagement(
        @Query("idRkb") idRkb: Int,
        @Query("adminMasterId") userId: Int
    ): Single<DetailScheduleManagementResponse>

    @Multipart
    @POST("api/v2/employee/management/bod/post/attendance-in")
    fun attendanceInGeoBodV2(
        @Query("bodId") userId: Int,
        @Query("idRkbBod") idRkbBod: Int,
        @Part file: MultipartBody.Part,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("address") address :String
    ): Single<AttendanceGeoBodResponse>

    @GET("api/v2/employee/management/bod/get/status")
    fun getAttendanceStatusBod(
        @Query("bodId") userId: Int,
        @Query("idRkbBod") idRkbOperation: Int
    ): Single<AttendanceStatusManagementResponse>

    @Multipart
    @PATCH("api/v2/employee/management/bod/patch/attendance-out")
    fun attendanceOutGeoBodV2(
        @Query("bodId") userId: Int,
        @Query("idRkbBod") idRkbBod: Int,
        @Part file: MultipartBody.Part,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("address") address :String
    ): Single<AttendanceGeoBodResponse>

    @GET("api/v1/project/search")
    fun getSearchProjectBranch(
        @Query("page") page: Int,
        @Query("branchCode") branchCode: String,
        @Query("keywords") keywords: String,
        @Query("perPage") perPage: Int
    ): Single<ProjectsAllAttendanceResponse>

}