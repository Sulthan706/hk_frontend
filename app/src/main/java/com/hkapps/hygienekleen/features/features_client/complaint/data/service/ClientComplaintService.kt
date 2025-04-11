package com.hkapps.hygienekleen.features.features_client.complaint.data.service

import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.CloseComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.complaintvisitorclient.ListCtalkVisitorClientResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkclient.DashboardCtalkClientResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient.DashboardCtalkVisitorResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.historyComplaint.HistoryComplaintResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.statusCreateComplaint.ValidateCreateComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.titleCreateComplaint.ListTitleCreateComplaintClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ClientComplaintService {
    @PUT("/api/v1/complaint/client/close/{complaintId}")
    fun putCloseComplaint(
        @Path("complaintId") complaintId: Int
    ): Single<CloseComplaintResponse>

    @Multipart
    @POST("/api/v1/complaint/create")
    fun postComplaintApi(
        @Query("userId") userId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: Int,
        @Query("description") description: String,
//        @Query("date") date: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part,
        @Part file4: MultipartBody.Part
    ): Single<ClientComplaintResponseModel>

    @GET("/api/v1/complaint/location/{projectId}")
    fun getComplaintAreaApi(
        @Path("projectId") projectId: String
    ): Single<ClientComplaintAreaResponseModel>

    @GET("/api/v1/complaint/sub-location/{projectId}/{locationId}")
    fun getComplaintSubAreaApi(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int
    ): Single<ClientComplaintSubAreaResponseModel>

    @GET("/api/v1/complaint/client/project")
    fun getHistoryComplaint(
        @Query("page") page: Int,
        @Query("projectCode") projectId: String,
        @Query("clientId") clientId: Int,
        @Query("complaintType") complaintType: ArrayList<String>
    ): Single<HistoryComplaintResponseModel>

    @GET("/api/v1/complaint/{complaintId}")
    fun getDetailHistoryComplaint(
        @Path("complaintId") complaintId: Int
    ): Single<DetailHistoryComplaintResponse>

    @GET("/api/v1/complaint/title")
    fun getTitleCreateComplaint(): Single<ListTitleCreateComplaintClientResponse>

    @GET("/api/v1/complaint/client/check")
    fun getValidateCreateCtalk(
        @Query("projectCode") projectCode: String
    ): Single<ValidateCreateComplaintResponse>

    @GET("/api/v1/complaint/client/project/visitor")
    fun getListCtalkVisitorClient(
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("filter") filter: String
    ): Single<ListCtalkVisitorClientResponseModel>

    @GET("/api/v1/complaint/client/dashboard-ctalk-klien")
    fun getDashboardCtalkClient(
        @Query("projectCode") projectCode: String,
        @Query("beginDate") beginDate: String,
        @Query("endDate") endDate: String
    ): Single<DashboardCtalkClientResponseModel>

    @GET("/api/v1/complaint/client/dashboard-ctalk-visitor")
    fun getDashboardCtalkVisitorClient(
        @Query("projectCode") projectCode: String,
        @Query("beginDate") beginDate: String,
        @Query("endDate") endDate: String
    ): Single<DashboardCtalkVisitorResponseModel>

}