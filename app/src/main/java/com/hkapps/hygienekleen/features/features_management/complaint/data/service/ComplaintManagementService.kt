package com.hkapps.hygienekleen.features.features_management.complaint.data.service

import com.hkapps.hygienekleen.features.features_management.complaint.model.createComplaint.CreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.dashboardcomplaintmanagement.DashboardManagementComplaintResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.detailComplaint.DetailComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listAreaComplaint.AreaCreatComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listBranch.ListBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.listComplaint.ListComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProject.ListProjectFmGmResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll.ListProjectAllResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listSubAreaComplaint.SubAreaCreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listTitleComplaint.TitleCreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listcomplaintctalk.ListCtalkManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.projectCount.ProjectCountCtalkManageResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ComplaintManagementService {

    @GET("/api/v1/management/project/v2/{id}")
    fun getListProjectFmGM(
        @Path("id") employeeId: Int
    ): Single<ListProjectFmGmResponse>

    @GET("/api/v1/complaint/info/count/{userId}/{projectId}")
    fun getProjectCountCtalkManage(
        @Path("userId") userId: Int,
        @Path("projectId") projectId: String
    ): Single<ProjectCountCtalkManageResponse>

    @GET("/api/v1/complaint/project")
    fun getComplaintManagement(
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("complaintType") complaintType: String
    ): Single<ListComplaintManagementResponse>

    @GET("/api/v1/complaint/{complaintId}")
    fun getDetailComplaintManagement(
        @Path("complaintId") complaintId: Int
    ): Single<DetailComplaintManagementResponse>

    @GET("/api/v1/project")
    fun getAllListProjectManagement(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ListProjectAllResponse>

    @GET("/api/v1/project/branch")
    fun getListBranch(): Single<ListBranchResponseModel>

    @GET("/api/v1/project/branch/branch-code")
    fun getListProjectByBranch(
        @Query("branchCode") branchCode: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Single<ListProjectAllResponse>

    @GET("/api/v1/project/search")
    fun getSearchProjectAll(
        @Query("page") page: Int,
        @Query("branchCode") branchCode: String,
        @Query("keywords") keywords: String,
        @Query("perPage") perPage: Int
    ): Single<ListProjectAllResponse>

    @Multipart
    @POST("/api/v1/complaint/management/create")
    fun postComplaintManagement(
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
    ): Single<CreateComplaintManagementResponse>

    @GET("/api/v1/complaint/title")
    fun getTitleCreateComplaint(): Single<TitleCreateComplaintManagementResponse>

    @GET("/api/v1/complaint/location/{projectId}")
    fun getComplaintAreaApi(
        @Path("projectId") projectId: String
    ): Single<AreaCreatComplaintManagementResponse>

    @GET("/api/v1/complaint/sub-location/{projectId}/{locationId}")
    fun getComplaintSubAreaApi(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int
    ): Single<SubAreaCreateComplaintManagementResponse>

    //ctalk
    @GET("/api/v1/complaint/project/ctalk")
    fun getCtalkManagement(
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("statusComplaint") statusComplaint: String
    ): Single<ListCtalkManagementResponseModel>

    @GET("/api/v1/complaint/ctalk/monthly-complaint")
    fun getDashboardComplaintManagement(
        @Query("projectCode") projectId: String
    ): Single<DashboardManagementComplaintResponseModel>

}