package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.detailMeeting.DetailMeetingInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listArea.ListAreaInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.ClientsMeetingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea.ListKontrolAreaResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.ListLaporanAreaResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listMeeting.ListMeetingInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listObject.ListObjectInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.submitFormMeeting.SubmitFormMeetingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.submitLaporanArea.SubmitLaporanAreaResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface InspeksiService {

    @Multipart
    @PUT("/api/v1/inspection/meeting/create-report")
    fun submitFormMeeting(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String,
        @Part file: MultipartBody.Part,
        @Query("fileDescription") fileDescription:String,
        @Query("emailParticipant") emailParticipant: ArrayList<String>,
        @Query("nameParticipant") nameParticipant: ArrayList<String>
    ): Single<SubmitFormMeetingResponse>

    @GET("/api/v1/inspection/meeting/list-meeting")
    fun getListMeeting(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int
    ): Single<ListMeetingInspeksiResponse>

    @GET("/api/v1/inspection/meeting/detail-meeting")
    fun getDetailMeeting(
        @Query("idMeeting") idMeeting: Int
    ): Single<DetailMeetingInspeksiResponse>

    @GET("/api/v1/inspection/area-control/list-area")
    fun getListAreaInspeksi(): Single<ListAreaInspeksiResponse>

    @GET("/api/v1/inspection/area-control/list-object")
    fun getListObjectInspeksi(
        @Query("idArea") idArea: Int
    ): Single<ListObjectInspeksiResponse>

    @Multipart
    @PUT("/api/v1/inspection/area-control/area-condition")
    fun submitLaporanArea(
        @Query("createdBy") createdBy: Int,
        @Query("projectCode") projectCode: String,
        @Query("idArea") idArea: Int,
        @Query("idObject") idObject: Int,
        @Part file: MultipartBody.Part,
        @Query("score") score: Int,
        @Query("description") description: String
    ): Single<SubmitLaporanAreaResponse>

    @GET("/api/v1/inspection/audit/list-report-hasil-kerja")
    fun getListLaporanArea(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("idArea") idArea: Int,
        @Query("date") date: String,
        @Query("page") page: Int
    ): Single<ListLaporanAreaResponse>

    @GET("/api/v1/inspection/area-control/list-report-area-control")
    fun getListKontrolArea(
        @Query("adminMasterId") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("typeVisit") typeVisit: String
    ): Single<ListKontrolAreaResponse>

    @GET("/api/v1/inspection/meeting/list-client")
    fun getListClientMeeting(
        @Query("projectCode") projectCode: String
    ): Single<ClientsMeetingResponse>

}