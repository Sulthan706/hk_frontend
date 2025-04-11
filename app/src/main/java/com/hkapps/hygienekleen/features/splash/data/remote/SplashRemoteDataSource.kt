package com.hkapps.hygienekleen.features.splash.data.remote

import com.hkapps.hygienekleen.features.splash.model.SplashModel
import io.reactivex.Single

interface SplashRemoteDataSource {
 fun getSplash(): Single<SplashModel>
}