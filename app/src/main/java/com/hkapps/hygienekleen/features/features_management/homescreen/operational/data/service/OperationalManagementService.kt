package com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational.BranchOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailattendance.DetailAttendanceOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailmanagement.DetailManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailoperational.DetailOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.giveratingemployee.GiveEmployeeRatingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance.HistoryAttendanceOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listalloperational.ListAllOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listceomanagement.ListCEOManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listemployee.ListOprtEmployeOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listgmfmom.ListGmOmFmResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listmanagement.ListOprtManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalbyprojectcode.ListOperationalByProjectCodeResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalproject.ListOperationalProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.ratingEmployee.RatingEmployeeResponse
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface OperationalManagementService {

    @GET("/api/v1/employee")
    fun getListAllOperational(
        @Query("page") page: Int
    ): Single<ListAllOperationalResponseModel>

    @GET("/api/v1/management/operational/list/all/job-role")
    fun getListOperationalByRoleCEO(
        @Query("jobRole") jobRole: String,
        @Query("page") page: Int
    ): Single<ListAllOperationalResponseModel>

    @GET("/api/v1/management/operational/list")
    fun getListOperationalProject(
        @Query("employeeId")employeeId: Int,
        @Query("page") page: Int
    ): Single<ListOperationalProjectResponseModel>

    @GET("/api/v1/management/operational/list/job-role")
    fun getListOperationalByRoleProject(
        @Query("employeeId")employeeId: Int,
        @Query("jobRole") jobRole: String,
        @Query("page") page: Int
    ): Single<ListOperationalProjectResponseModel>

    @GET("/api/v1/management/ceo/list/employee")
    fun getListAllOperationalCEO(
        @Query("page") page: Int
    ): Single<ListCEOManagementResponseModel>

    @GET("/api/v1/management/bod/list/employee/")
    fun getListAllOperationalBOD(
        @Query("page") page: Int
    ): Single<ListCEOManagementResponseModel>

    @GET("/api/v1/management/list/all/job-role")
    fun getListManagementByRoleCeoBod(
        @Query("jabatan") jabatan: String
    ): Single<ListGmOmFmResponseModel>

    @GET("/api/v1/management/list/job-role")
    fun getListManagementByRoleFmGmOm(
        @Query("employeeId") employeeId: Int,
        @Query("jabatan") jabatan: String
    ): Single<ListGmOmFmResponseModel>

    @GET("/api/v1/management/list/employee/role")
    fun getListGmOmFm(
        @Query("employeeId") employeeId: Int
    ): Single<ListGmOmFmResponseModel>

    @GET("/api/v1/employee/profile")
    fun getDetailOperational(
        @Query("employeeId") employeeId: Int
    ): Single<DetailOperationalResponseModel>

    @GET("/api/v1/management/{adminMasterId}")
    fun getDetailManagement(
        @Path("adminMasterId") adminMasterId: Int
    ): Single<DetailManagementResponseModel>

    @GET("/api/v1/attendance/employee/info")
    fun getDetailOperationalAttendance(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<DetailAttendanceOperationalResponseModel>

    @GET("/api/v1/employee/attendance/history")
    fun getHistoryAttendance(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<HistoryAttendanceOperationalResponseModel>

    @GET("/api/v1/employee/search")
    fun searchOperationalCeoBod(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ListAllOperationalResponseModel>

    @GET("/api/v1/management/operational/list/search")
    fun searchOperationalGmOmFm(
        @Query("employeeId")employeeId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ListOperationalProjectResponseModel>

    @GET("/api/v1/management/ceo/list/employee/search")
    fun searchManagementUserCeo(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ListCEOManagementResponseModel>

    @GET("/api/v1/management/bod/list/employee/search")
    fun searchManagementUserBod(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ListCEOManagementResponseModel>

    @GET("/api/v1/management/list/employee/role/search")
    fun searchManagementUserGmOmFm(
        @Query("employeeId") employeeId: Int,
        @Query("keywords") keywords: String
    ): Single<ListGmOmFmResponseModel>

    @GET("/api/v1/project/employee/job-role")
    fun getListOperationalByProjectCode(
        @Query("jobRole") jobRole: String,
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int
    ): Single <ListOperationalByProjectCodeResponseModel>

    @GET("/api/v1/rating/employee/{employeeId}")
    fun getProfileRating(
        @Path("employeeId") employeeId: Int
    ): Single<RatingEmployeeResponse>

    @Multipart
    @POST("/api/v1/rating/employee")
    fun giveRatingEmployee(
        @Part("ratingByUserId") ratingByUserId: RequestBody,
        @Part("employeeId") employeeId: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("projectCode") projectCode: RequestBody,
        @Part("jobCode") jobCode: RequestBody
    ): Single<GiveEmployeeRatingResponse>

    @GET("api/v1/bod/operational/get/branch/employee")
    fun getBranchOperational(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BranchOperationalResponseModel>

    @GET("api/v1/bod/operational/get/branch/management")
    fun getBranchManagementOperational(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BranchOperationalResponseModel>

    @GET("/api/v1/bod/operational/get/management")
    fun getListManagementOperational(
        @Query("idCabang") idCabang: Int,
        @Query("role") role: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ListOprtManagementResponseModel>

    @GET("/api/v1/bod/operational/get/employee")
    fun getListEmployeeOperational(
        @Query("branchCode") branchCode: String,
        @Query("role") role: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ListOprtEmployeOperationalResponseModel>
}