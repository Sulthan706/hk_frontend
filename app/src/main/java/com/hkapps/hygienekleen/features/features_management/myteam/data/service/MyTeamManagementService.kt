package com.hkapps.hygienekleen.features.features_management.myteam.data.service

import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.ListBranchMyTeamResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.ListChiefSpvMyTeamManagementResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.ListManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.ListProjectManagementResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyTeamManagementService {

    @GET("/api/v1/project/branch/branch-code")
    fun getListProjectManagement(
        @Query("branchCode") branchCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ListProjectManagementResponse>

    @GET("/api/v1/project/chief-spv/team")
    fun getListChiefManagement(
        @Query("projectId") projectId: String
    ): Single<ListChiefSpvMyTeamManagementResponse>

    @GET("/api/v1/project/branch")
    fun getListBranch(): Single<ListBranchMyTeamResponse>

    @GET("/api/v1/management/structural/{projectCode}")
    fun getListManagement(
        @Path("projectCode") projectCode: String
    ): Single<ListManagementResponseModel>

    @GET("/api/v1/project/search")
    fun searchListProject(
        @Query("page") page: Int,
        @Query("branchCode") branchCode: String,
        @Query("keywords") keywords: String,
        @Query("perPage") perPage: Int
    ): Single<ListProjectManagementResponse>

}