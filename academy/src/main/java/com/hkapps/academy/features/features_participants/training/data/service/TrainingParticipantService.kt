package com.hkapps.academy.features.features_participants.training.data.service

import com.hkapps.academy.features.features_participants.training.model.listTraining.TrainingsCalendarResponse
import com.hkapps.academy.features.features_participants.training.model.scheduleTraining.ScheduleTrainingResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainingParticipantService {

    @GET("/api/v1/academy/participant/training/get/event-calendar")
    fun getScheduleTraining(
        @Query("userNuc") userNuc: String,
        @Query("projectCode") projectCode: String,
        @Query("levelJabatan") levelJabatan: String,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<ScheduleTrainingResponse>

    @GET("/api/v1/academy/participant/training/get/all")
    fun getListTraining(
        @Query("userNuc") userNuc: String,
        @Query("projectCode") projectCode: String,
        @Query("levelJabatan") levelJabatan: String,
        @Query("date") date: String,
        @Query("region") region: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<TrainingsCalendarResponse>

}