package com.hkapps.hygienekleen.features.features_management.damagereport.data.service

import com.hkapps.hygienekleen.features.features_management.damagereport.model.detaildamagereport.DetailDamageReportMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport.ListDamageReportResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listprojectdamagereport.ListProjectBakResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploaddamageimagebak.PutDamageBakMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadfotodamagereport.UploadFotoBakResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadfrontimagebak.PutFrontBakMgmntReponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadketerangan.UploadKeteranganResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DamageReportManagementService {

    @GET("/api/v1/mesin/manajemen/get-bak-mesin")
    fun getListDamageReportManagement(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("projectCode") projectCode : String,
        @Query("date") date: String,
        @Query("filter") filter: String,
        @Query("page") page: Int
    ): Single<ListDamageReportResponseModel>

    @GET("/api/v1/mesin/get-detail-bak-mesin")
    fun getDetailDamageReportManagement(
        @Query("idDetailBakMesin") idDetailBakMesin:Int
    ):Single<DetailDamageReportMgmntResponseModel>

    @GET("/api/v1/project/search/own/new/{adminMasterId}")
    fun getListProjectDamageReportManagement(
        @Path("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ListProjectBakResponseModel>

    @Multipart
    @POST("/api/v1/mesin/management/upload-post")
    fun putUploadBakManagement(
        @Query("idDetailBakMesin") idDetailBakMesin: Int,
        @Query("adminMasterId") adminMasterId: Int,
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Query("keteranganAssets") keteranganAssets: String
    ): Single<UploadFotoBakResponseModel>

    @Multipart
    @PUT("/api/v1/mesin/management/put/foto-depan")
    fun putUploadFrontBakManagement(
        @Query("idDetailBakMesin") idDetailBakMesin: Int,
        @Query("adminMasterId") adminMasterId: Int,
        @Part file1: MultipartBody.Part
    ): Single<PutFrontBakMgmntReponseModel>

    @Multipart
    @PUT("/api/v1/mesin/management/put/foto-kerusakan")
    fun putUploadDamageBakManagement(
        @Query("idDetailBakMesin") idDetailBakMesin: Int,
        @Query("adminMasterId") adminMasterId: Int,
        @Part file2: MultipartBody.Part
    ): Single<PutDamageBakMgmntResponseModel>

    @PUT("/api/v1/mesin/management/put/keterangan")
    fun putKeteranganBakManagement(
        @Query("idDetailBakMesin") idDetailBakMesin: Int,
        @Query("adminMasterId") adminMasterId: Int,
        @Query("keteranganAssets") keteranganAssets: String
    ): Single<UploadKeteranganResponseModel>

    @GET("api/v1/mesin/home/list-mesin")
    fun getDataListBAKMachine(
        @Query("projectCode") projectCode : String,
        @Query("page") page : Int,
        @Query("perPage") size : Int,
    ):Single<BakMachineResponse>



}