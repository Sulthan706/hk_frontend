package com.hkapps.hygienekleen.features.splash.di

import com.hkapps.hygienekleen.features.splash.data.remote.SplashRemoteDataSource
import com.hkapps.hygienekleen.features.splash.data.remote.SplashRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.splash.data.repository.SplashRepository
import com.hkapps.hygienekleen.features.splash.data.repository.SplashRepositoryImpl
import com.hkapps.hygienekleen.features.splash.data.service.SplashService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SplashModule {
    @Provides
    @Singleton
    fun provideRepository(splashRemoteDataSource: SplashRemoteDataSource): SplashRepository {
        return SplashRepositoryImpl(splashRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(splashService: SplashService): SplashRemoteDataSource{
        return SplashRemoteDataSourceImpl(splashService)
    }

    @Provides
    @Singleton
    fun provideSplashService(): SplashService{
        return AppRetrofit.splashService
    }
}