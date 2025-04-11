package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.complaintsByEmployee.ComplaintsByEmployeeResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.createComplaint.CreateCftalkManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.ProjectsAllCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listLocation.LocationsProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listProjectByUserId.ProjectsEmployeeIdResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listSubLoc.SubLocationProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listTitleComplaint.TitlesCftalkManagementResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface CftalkManagementService {

    @GET("/api/v1/management/project/v2/{id}")
    fun getProjectsByEmployeeId(
        @Path("id") employeeId: Int
    ): Single<ProjectsEmployeeIdResponse>

    @GET("/api/v1/project")
    fun getAllProject(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ProjectsAllCftalkResponse>

    @GET("/api/v1/complaint/management/employee/{employeeId}")
    fun getComplaintByEmployee(
        @Path("employeeId") employeeId: Int,
        @Query("page") page: Int
    ): Single<ComplaintsByEmployeeResponse>

    @GET("/api/v1/complaint/management/{projectId}")
    fun getComplaintByProject(
        @Path("projectId") projectId: String,
        @Query("page") page: Int
    ): Single<ComplaintsByEmployeeResponse>

    @GET("/api/v1/complaint/title")
    fun getTitleComplaint(): Single<TitlesCftalkManagementResponse>

    @GET("/api/v1/complaint/location/{projectId}")
    fun getLocationComplaint(
        @Path("projectId") projectId: String
    ): Single<LocationsProjectCftalkResponse>

    @GET("/api/v1/complaint/sub-location/{projectId}/{locationId}")
    fun getSubLocationComplaint(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int
    ): Single<SubLocationProjectCftalkResponse>

    @Multipart
    @POST("/api/v1/complaint/management/create")
    fun createCftalkManagement(
        @Query("userId") userId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: Int,
        @Query("description") description: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part,
        @Part file4: MultipartBody.Part,
        @Query("complaintType") complaintType: String
    ): Single<CreateCftalkManagementResponse>

    @GET("/api/v1/project/employee/{userId}")
    fun getOtherProjects(
        @Path("userId") userId: Int,
        @Query("page") page: Int
    ): Single<ProjectsAllCftalkResponse>

    @GET("/api/v1/project/search/{userId}")
    fun getSearchOtherProjects(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ProjectsAllCftalkResponse>

    @GET("/api/v1/project/search/all")
    fun getSearchAllProject(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ProjectsAllCftalkResponse>

    @GET("/api/v1/project/branch/branch-code")
    fun getProjectByBranch(
        @Query("branchCode") branchCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsAllCftalkResponse>

    @GET("api/v1/project/search")
    fun getSearchProjectBranch(
        @Query("page") page: Int,
        @Query("branchCode") branchCode: String,
        @Query("keywords") keywords: String,
        @Query("perPage") perPage: Int
    ): Single<ProjectsAllCftalkResponse>

}