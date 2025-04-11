package com.hkapps.academy.features.features_participants.classes.data.service

import com.hkapps.academy.features.features_participants.classes.model.listClass.ClassesParticipantResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ClassParticipantService {

    @GET("/api/v1/academy/participant/kelas/get/all")
    fun getListClassParticipant(
        @Query("userNuc") userNuc: String,
        @Query("projectCode") projectCode: String,
        @Query("levelJabatan") levelJabatan: String,
        @Query("date") date: String,
        @Query("region") region: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ClassesParticipantResponse>

}