package com.hkapps.hygienekleen.features.splash.data.repository
import com.hkapps.hygienekleen.features.splash.data.remote.SplashRemoteDataSource
import com.hkapps.hygienekleen.features.splash.model.SplashModel
import io.reactivex.Single
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(private val remoteDataSource: SplashRemoteDataSource): SplashRepository {
    override fun getSplashScreen(): Single<SplashModel> {
        return remoteDataSource.getSplash()
    }
}