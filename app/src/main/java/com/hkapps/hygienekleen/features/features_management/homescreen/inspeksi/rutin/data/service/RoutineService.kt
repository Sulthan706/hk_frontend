package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.detailRoutine.DetailRoutineReportResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.ClientsRoutineResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.ListRoutineVisitedResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.submitRoutine.SubmitFormRoutineResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface RoutineService {

    @GET("/api/v1/inspection/meeting/list-client")
    fun getListClient(
        @Query("projectCode") projectCode: String
    ): Single<ClientsRoutineResponse>

    @Multipart
    @PUT("/api/v1/inspection/routine/create-routine")
    fun submitFormRoutine(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Part file: MultipartBody.Part,
        @Query("fileDescription") fileDescription:String,
        @Query("emailParticipant") emailParticipant: ArrayList<String>
    ): Single<SubmitFormRoutineResponse>

    @GET("/api/v1/inspection/routine/list-routine")
    fun getListRoutine(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int
    ): Single<ListRoutineVisitedResponse>

    @GET("/api/v1/inspection/routine/detail-routine")
    fun getDetailRoutine(
        @Query("idRoutine") idRoutine: Int
    ): Single<DetailRoutineReportResponse>

}