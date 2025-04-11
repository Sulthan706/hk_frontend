package com.hkapps.hygienekleen.features.features_client.overtime.data.service

import com.hkapps.hygienekleen.features.features_client.overtime.model.createOvertime.CreateOvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeChangeClient.DetailOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeRequestClient.DetailOvertimeRequestClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listLocation.LocationOvertimeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeChangeClient.ListOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeReqClient.OvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listSubLoc.SubLocOvertimeClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface OvertimeClientService {

    @Multipart
    @POST("/api/v1/overtime/tagih/create")
    fun createOvertimeClient(
        @Query("createdById") employeeId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Query("date") date: String,
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String,
        @Part file: MultipartBody.Part,
        @Query("totalWorker") totalWorker: Int
    ): Single<CreateOvertimeReqClientResponse>

    @GET("/api/v2/overtime/ganti")
    fun getListOvertimeChangeClient(
        @Query("projectId") projectId: String,
        @Query("employeeId") employeeId: Int,
        @Query("page") page: Int
    ): Single<ListOvertimeChangeClientResponse>

    @GET("/api/v2/overtime/ganti/{overtimeId}")
    fun getDetailOvertimeChangeClient(
        @Path("overtimeId") overtimeId: Int
    ): Single<DetailOvertimeChangeClientResponse>

    @GET("/api/v1/project/location/{projectId}")
    fun getLocationOvertimeClient(
        @Path("projectId") projectId: String
    ): Single<LocationOvertimeClientResponse>

    @GET("/api/v1/project/sub-location/{projectId}/{locationId}")
    fun getSubLocOvertimeClient(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int
    ): Single<SubLocOvertimeClientResponse>

    @GET("/api/v1/overtime/all")
    fun getListOvertimeReqClient(
        @Query("projectId") projectId: String,
        @Query("page") page: Int
    ): Single<OvertimeReqClientResponse>

    @GET("/api/v1/overtime/{overtimeTagihId}")
    fun getDetailOvertimeRequestClient(
        @Path("overtimeTagihId") overtimeTagihId: Int
    ): Single<DetailOvertimeRequestClientResponse>

}