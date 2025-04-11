package com.hkapps.hygienekleen.features.features_vendor.damagereport.data.service

import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.checkusertechnician.CheckUserVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.detailbakvendor.DetailBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.uploadbakvendor.UploadBakVendorResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface DamageReportVendorService {
    @GET("/api/v1/mesin/get-bak-mesin")
    fun getListBakVendor(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int
    ): Single<ListBakVendorResponseModel>

    @GET("/api/v1/mesin/get-detail-bak-mesin")
    fun getDetailBakVendor(
        @Query("idDetailBakMesin") idDetailBakMesin: Int
    ): Single<DetailBakVendorResponseModel>

    @Multipart
    @PUT("/api/v1/mesin/employee/upload-bak")
    fun putUploadBakVendor(
        @Query("idDetailBakMesin") idDetailBakMesin: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part,
        @Query("keteranganBak") keteranganBak: String
    ): Single<UploadBakVendorResponseModel>

    @GET("/api/v1/mesin/employee/validate-access")
    fun getCheckUserTechnician(
        @Query("employeeId") employeeId: Int
    ): Single<CheckUserVendorResponseModel>

    @GET("api/v1/mesin/home/list-mesin")
    fun getDataListBAKMachine(
        @Query("projectCode") projectCode : String,
        @Query("page") page : Int,
        @Query("perPage") size : Int,
    ):Single<BakMachineResponse>

}