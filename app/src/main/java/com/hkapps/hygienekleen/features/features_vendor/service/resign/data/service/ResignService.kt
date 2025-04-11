package com.hkapps.hygienekleen.features.features_vendor.service.resign.data.service

import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.detailresignvendor.DetailReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.getdatevendor.DateResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.ListResignVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.submitresign.SubmitResignVendorResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ResignService {

    @GET("api/v1/resign/employee/get/all")
    fun getListResignVendor(
        @Query("employeeId") employeeId: Int
    ): Single<ListResignVendorResponseModel>

    @POST("api/v1/resign/employee/post/assign")
    fun postSubmitResignVendor(
        @Query("employeeId") employeeId: Int,
        @Query("tanggalPengajuan") tanggalPengajuan: String
     ): Single<SubmitResignVendorResponseModel>

    @GET("api/v1/resign/employee/get/min-days")
    fun getDateResignVendor(
        @Query("projectCode") projectCode: String
    ): Single<DateResignResponseModel>

    @GET("api/v1/resign/employee/get/detail")
    fun getDetailResignVendor(
        @Query("idTurnOver") idTurnOver: Int
    ): Single<DetailReasonResignResponseModel>


}