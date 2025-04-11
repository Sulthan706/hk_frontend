package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.createweekly.CreateWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.detailweekly.DetailWeeklyResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.ListWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.validation.ValidationResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface WeeklyProgressService {

    @GET("/api/v1/inspection/weekly/get/list")
    fun getListWeeklyProgress(
        @Query("projectCode") projectCode: String,
        @Query("adminMasterId") adminMasterId: Int,
        @Query("startAt") startAt: String,
        @Query("endAt") endAt: String,
        @Query("status") status: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ):Single<ListWeeklyProgressResponse>

    @GET("/api/v1/inspection/weekly/get/detail")
    fun getDetailWeeklyProgress(
        @Query("idWeekly") idWeekly: Int,
    ):Single<DetailWeeklyResponse>

    @Multipart
    @POST("/api/v1/inspection/weekly/post/create")
    fun createWeeklyProgress(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("projectCode") projectCode: String,
        @Query("location") location : String,
        @Query("materialType") materialType : String,
        @Query("chemical") chemical : String,
        @Query("volumeChemical") volumeChemical : Int,
        @Query("cleaningMethod") cleaningMethod : String,
        @Query("frequency") frequency : Int,
        @Query("areaSize") areaSize : Int,
        @Query("totalPic") totalPic : Int,
        @Part fileBefore : MultipartBody.Part,
        @Part fileAfter : MultipartBody.Part?,
        @Query("status") status : String,
    ):Single<CreateWeeklyProgressResponse>

    @Multipart
    @PUT("/api/v1/inspection/weekly/put/edit")
    fun updateWeeklyProgress(
        @Query("idWeekly") idWeekly: Int,
        @Query("adminMasterId") adminMasterId: Int,
        @Query("projectCode") projectCode: String,
        @Query("location") location : String,
        @Query("materialType") materialType : String,
        @Query("chemical") chemical : String,
        @Query("volumeChemical") volumeChemical : Int,
        @Query("cleaningMethod") cleaningMethod : String,
        @Query("frequency") frequency : Int,
        @Query("areaSize") areaSize : Int,
        @Query("totalPic") totalPic : Int,
        @Part fileBefore : MultipartBody.Part,
        @Part fileAfter : MultipartBody.Part?,
        @Query("status") status : String,
    ):Single<CreateWeeklyProgressResponse>


    @GET("/api/v1/inspection/weekly/get/validation")
    fun checkValidation(
        @Query("adminMasterId") userId: Int,
        @Query("date") date : String
    ): Single<ValidationResponse>

}