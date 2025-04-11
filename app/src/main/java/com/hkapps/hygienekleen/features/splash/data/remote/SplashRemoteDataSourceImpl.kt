package com.hkapps.hygienekleen.features.splash.data.remote;
import com.hkapps.hygienekleen.features.splash.data.service.SplashService
import com.hkapps.hygienekleen.features.splash.model.SplashModel
import io.reactivex.Single
import javax.inject.Inject

class SplashRemoteDataSourceImpl @Inject constructor(private val service: SplashService):SplashRemoteDataSource{
    override fun getSplash(): Single<SplashModel>{
        return service.getSplash()
    }
}
