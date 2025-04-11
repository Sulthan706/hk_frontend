package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.service

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.divertionSchedule.DivertionScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProject.ProjectAllScheduleResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProjectSchedule.ProjectsScheduleManagementModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listSchedule.SchedulesManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listScheduleBod.SchedulesBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiBod
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiManagement
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiTeknisi
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleBod.SubmitCreateScheduleBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleManagement.SubmitCreateScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleTeknisi.SubmitCreateScheduleTeknisiResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleManagementService {

    @GET("api/v2/employee/management/get/schedules")
    fun getScheduleManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<SchedulesManagementResponse>

    @GET("/api/v1/employee/management/project/{adminMasterId}")
    fun getProjectsSchedule(
        @Path("adminMasterId") userId: Int,
        @Query("page") page: Int,
        @Query("size") size : Int
    ): Single<ProjectAllScheduleResponse>

    @GET("/api/v1/project")
    fun getAllProjectSchedule(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ProjectAllScheduleResponse>

    @PATCH("/api/v2/employee/management/patch/divertion")
    fun divertionSchedule(
        @Query("adminMasterId") userId: Int,
        @Query("idRkbOperation") idRkbOperation: Int,
        @Query("divertedTo") divertedTo: String,
        @Query("reason") reason: String
    ): Single<DivertionScheduleManagementResponse>

    @GET("/api/v1/bod/schedule/list-option")
    fun getScheduleBod(
        @Query("bodId") userId: Int,
        @Query("date") date: String
    ): Single<SchedulesBodResponse>

    @PATCH("/api/v2/employee/management/patch/divertion/bod")
    fun diversionScheduleBod(
        @Query("adminMasterId") userId: Int,
        @Query("idRkbBod") idRkbBod: Int,
        @Query("divertedTo") divertedTo: String,
        @Query("reason") reason: String
    ): Single<DivertionScheduleManagementResponse>

    @GET("/api/v1/project/branch/branch-code")
    fun getProjectVp(
        @Query("branchCode") branchCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectAllScheduleResponse>

    @POST("/api/v1/bod/add-rkb")
    fun submitCreateScheduleBod(
        @Body data: ArrayList<ProjectsScheduleApiBod>
    ): Single<SubmitCreateScheduleBodResponse>

    @POST("/api/v1/management/add-rkb")
    fun submitCreateScheduleManagement(
        @Body data: ArrayList<ProjectsScheduleApiManagement>
    ): Single<SubmitCreateScheduleManagementResponse>

    @POST("/api/v1/management/add-rkb/teknisi")
    fun submitCreateScheduleTeknisi(
        @Body data: ArrayList<ProjectsScheduleApiTeknisi>
    ): Single<SubmitCreateScheduleTeknisiResponse>

    @GET("/api/v1/rkb/management/list-project/vp")
    fun getProjectsScheduleVp(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("cabang") branchCode: String,
        @Query("keywords") query: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsScheduleManagementModel>

    @GET("/api/v1/rkb/management/list-project/bod")
    fun getProjectsScheduleBod(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("keywords") query: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsScheduleManagementModel>

    @GET("/api/v1/rkb/management/list-project/teknisi")
    fun getProjectsScheduleTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("keywords") query: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsScheduleManagementModel>

    @GET("/api/v1/rkb/management/list-project")
    fun getProjectsScheduleManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("keywords") query: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsScheduleManagementModel>

}