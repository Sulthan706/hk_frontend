package com.hkapps.hygienekleen.features.splash.data.service

import com.hkapps.hygienekleen.features.splash.model.SplashModel
import io.reactivex.Single
import retrofit2.http.GET

interface SplashService {
    @GET("/register")
    fun getSplash(): Single<SplashModel>
}