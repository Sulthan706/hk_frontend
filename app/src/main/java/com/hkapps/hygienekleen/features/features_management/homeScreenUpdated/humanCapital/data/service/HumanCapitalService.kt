package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.service

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.BranchesHumanCapitalResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.ManPowerBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.ManPowerManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.submitRating.SubmitRatingResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HumanCapitalService {

    @GET("api/v1/management/operational/list/v2")
    fun getManPowerManagement(
        @Query("employeeId") userId: Int,
        @Query("keywords") keywords: String,
        @Query("filter") filter: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ManPowerManagementResponse>

    @GET("api/v1/bod/employee/list")
    fun getBranchesHumanCapital(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<BranchesHumanCapitalResponse>

    @GET("api/v1/bod/employee/list-perbranch")
    fun getManPowerBod(
        @Query("kodeCabang") branchCode: String,
        @Query("employeeName") keywords: String,
        @Query("filter") filter: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ManPowerBodResponse>

    @POST("api/v1/rating/v2/employee")
    fun submitRating(
        @Query("ratingByUserId") ratingByUserId: Int,
        @Query("roleUser") roleUser: String,
        @Query("employeeId") employeeId: Int,
        @Query("rating") rating: Int,
        @Query("feedback") feedback: String,
        @Query("projectCode") projectCode: String,
        @Query("jobCode") jobCode: String
    ): Single<SubmitRatingResponse>

}