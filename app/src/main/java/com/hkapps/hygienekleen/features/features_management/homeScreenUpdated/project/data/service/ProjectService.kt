package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.service

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.BranchesProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.ProjectsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.ProjectsManagementResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectService {

    @GET("/api/v1/bod/project/list")
    fun getBranchesProject(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<BranchesProjectManagementResponse>

    @GET("/api/v1/management/project/list/v2")
    fun getProjectsManagement(
        @Query("employeeId") userId: Int,
        @Query("keywords") keywords: String,
        @Query("filter") filter: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsManagementResponse>

    @GET("/api/v1/management/project/list-teknisi/v2")
    fun getProjectsTeknisi(
        @Query("employeeId") userId: Int,
        @Query("keywords") keywords: String,
        @Query("filter") filter: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsManagementResponse>

    @GET("/api/v1/bod/project/list-perbranch")
    fun getProjectBod(
        @Query("kodeCabang") branchCode: String,
        @Query("filter") filter: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ProjectsBodResponse>

}