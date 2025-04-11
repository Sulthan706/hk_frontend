package com.hkapps.academy.features.authentication.data.service

import com.hkapps.academy.features.authentication.model.login.LoginAcademyResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAcademyService {

    @POST("/api/v1/academy/auth/login")
    fun loginAcademy(
        @Query("userNuc") userNuc: String
    ): Single<LoginAcademyResponse>

}