package com.hkapps.academy.features.features_participants.homescreen.home.data.service

import com.hkapps.academy.features.features_participants.homescreen.home.model.listClass.ClassesHomeResponse
import com.hkapps.academy.features.features_participants.homescreen.home.model.listTraining.TrainingsHomeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeParticipantService {

    @GET("/api/v1/academy/participant/kelas/get/all")
    fun getListClassHome(
        @Query("userNuc") userNuc: String,
        @Query("projectCode") projectCode: String,
        @Query("levelJabatan") levelJabatan: String,
        @Query("date") date: String,
        @Query("region") region: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ClassesHomeResponse>

    @GET("/api/v1/academy/participant/training/get/all")
    fun getListTrainingHome(
        @Query("userNuc") userNuc: String,
        @Query("projectCode") projectCode: String,
        @Query("levelJabatan") levelJabatan: String,
        @Query("date") date: String,
        @Query("region") region: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<TrainingsHomeResponse>

}