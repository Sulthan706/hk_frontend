package com.hkapps.hygienekleen.features.splash.data.repository
import com.hkapps.hygienekleen.features.splash.model.SplashModel
import io.reactivex.Single

interface SplashRepository {
    fun getSplashScreen(): Single<SplashModel>
}