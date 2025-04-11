package com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.service

import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.dashboardctalkvendor.DashboardCtalkVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.HistoryComplaintResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface VendorComplaintService {
    @Multipart
    @POST("/api/v1/complaint/internal/create")
    fun postComplaintApi(
        @Query("userId") userId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
//        @Query("date") date: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Part file: MultipartBody.Part,
    ): Single<VendorComplaintResponseModel>

    @GET("/api/v1/complaint/location/{projectId}")
    fun getComplaintAreaApi(
        @Path("projectId") projectId: String
    ): Single<VendorComplaintAreaResponseModel>

    @GET("/api/v1/complaint/sub-location/{projectId}/{locationId}")
    fun getComplaintSubAreaApi(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int
    ): Single<VendorComplaintSubAreaResponseModel>

    @GET("/api/v1/complaint/project")
    fun getHistoryComplaint(
        @Query("page") page: Int,
        @Query("projectCode") projectId: String,
        @Query("complaintType") complaintType: String
    ): Single<HistoryComplaintResponseModel>

    @GET("/api/v1/complaint/{complaintId}")
    fun getDetailHistoryComplaint(
        @Path("complaintId") complaintId: Int
    ): Single<DetailHistoryComplaintResponse>

    @GET("/api/v1/complaint/ctalk/monthly-complaint")
    fun getDashboardComplaint(
        @Query("projectCode") projectId: String
    ): Single<DashboardCtalkVendorResponseModel>
}