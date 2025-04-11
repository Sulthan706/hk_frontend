package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.service

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.lastVisit.VisitHomeManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.RemainingVisitsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.RemainingVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitTeknisi.RemainingVisitsTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.rkbHome.RkbHomeManagementResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeManagementUpdatedService {

    @GET("/api/v1/rkb/management/home/bod")
    fun getRkbBodHome(
        @Query("bodId") bodId: Int,
        @Query("date") date: String
    ): Single<RkbHomeManagementResponse>

    @GET("/api/v1/bod/attendance/activities")
    fun getLastVisitBod(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("date") date: String
    ): Single<VisitHomeManagementResponse>

    @GET("/api/v1/bod/schedule/list")
    fun getListRemainingVisitBod(
        @Query("bodId") bodId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<RemainingVisitsBodResponse>

    @GET("/api/v1/rkb/management/home/new")
    fun getRkbManagementHome(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String
    ): Single<RkbHomeManagementResponse>

    @GET("/api/v1/management/attendance/activities")
    fun getLastVisitManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String
    ): Single<VisitHomeManagementResponse>

    @GET("/api/v1/management/schedule/list")
    fun getListRemainingVisitManagement(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<RemainingVisitsManagementResponse>

    @GET("/api/v1/rkb/management/home/teknisi")
    fun getRkbTeknisiHome(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String
    ): Single<RkbHomeManagementResponse>

    @GET("/api/v1/management/attendance/activities/teknisi")
    fun getLastVisitTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String
    ): Single<VisitHomeManagementResponse>

    @GET("/api/v1/management/schedule/list/teknisi")
    fun getListRemainingVisitTeknisi(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<RemainingVisitsTeknisiResponse>

}