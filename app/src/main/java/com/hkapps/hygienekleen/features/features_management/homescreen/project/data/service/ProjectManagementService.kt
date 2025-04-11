package com.hkapps.hygienekleen.features.features_management.project.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listClient.ListClientProjectMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listbranch.ListAllBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject.ListProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.ListProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.attendanceProject.AttendanceProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.complaintProject.ComplaintProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.detailProject.DetailProjectManagementResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectManagementService {

    @GET("/api/v1/project/branch/branch-code")
    fun getListProjectManagement(
        @Query("branchCode") branchCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ListProjectManagementResponseModel>

    @GET("/api/v1/management/project/v2/{id}")
    fun getProjectCode(
        @Path("id") projectId: Int
    ): Single<ListProjectResponseModel>

    @GET("/api/v1/project/branch")
    fun getListBranch(): Single<ListAllBranchResponseModel>

    @GET("/api/v1/project/search")
    fun getSearchProjectAll(
        @Query("page") page: Int,
        @Query("branchCode") branchCode: String,
        @Query("keywords") keywords: String,
        @Query("perPage") perPage: Int
    ): Single<ListProjectManagementResponseModel>

    @GET("/api/v1/project/{projectCode}")
    fun getDetailProject(
        @Path ("projectCode") projectCode: String
    ): Single<DetailProjectManagementResponse>

    @GET("/api/v1/complaint/info/count/{userId}/{projectId}")
    fun getComplaintProject(
        @Path("userId") userId: Int,
        @Path("projectId") projectId: String,
        @Query("complaintType") complaintType: ArrayList<String>
    ): Single<ComplaintProjectManagementResponse>

    @GET("/api/v1/attendance/project/info")
    fun getAttendanceProject(
        @Query("projectCode") projectCode: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<AttendanceProjectManagementResponse>

    @GET("/api/v1/project/client/{projectCode}")
    fun getListClientProject(
        @Path("projectCode") projectCode: String
    ): Single<ListClientProjectMgmntResponse>

}