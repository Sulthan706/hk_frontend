package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.remote.PlottingRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.remote.PlottingRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.repository.PlottingRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.repository.PlottingRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.service.PlottingService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlottingModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: PlottingRemoteDataSource): PlottingRepository {
        return PlottingRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: PlottingService): PlottingRemoteDataSource {
        return PlottingRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): PlottingService {
        return AppRetrofit.plottingService
    }
}