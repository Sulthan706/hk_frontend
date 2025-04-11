package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.remote.HomeRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.remote.HomeRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository.HomeRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository.HomeRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.service.HomeService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeModule {

    @Provides
    @Singleton
    fun provideRepository(homeRemoteDataSource: HomeRemoteDataSource): HomeRepository {
        return HomeRepositoryImpl(homeRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(homeService: HomeService): HomeRemoteDataSource {
        return HomeRemoteDataSourceImpl(homeService)
    }

    @Provides
    @Singleton
    fun provideHomeService(): HomeService {
        return AppRetrofit.homeService
    }
}