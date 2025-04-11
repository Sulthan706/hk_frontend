package com.hkapps.academy.features.features_trainer.homescreen.home.data.service

import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.ClassesHomeTrainerResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeTrainerService {

    @GET("/api/v1/academy/trainer/training/get/all")
    fun getListClassHomeTrainer(
        @Query("userNuc") userNuc: String,
        @Query("date") date: String,
        @Query("region") region: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ClassesHomeTrainerResponse>

}