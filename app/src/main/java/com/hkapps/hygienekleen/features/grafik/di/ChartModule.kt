package com.hkapps.hygienekleen.features.grafik.di

import com.hkapps.hygienekleen.features.grafik.data.remote.ChartRemoteDataSource
import com.hkapps.hygienekleen.features.grafik.data.remote.ChartRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.grafik.data.repository.ChartRepository
import com.hkapps.hygienekleen.features.grafik.data.repository.ChartRepositoryImpl
import com.hkapps.hygienekleen.features.grafik.data.service.ChartService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChartModule {

    @Provides
    @Singleton
    fun providesRepository(
        remoteDataSource : ChartRemoteDataSource,
    ): ChartRepository {
        return ChartRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service : ChartService): ChartRemoteDataSource {
        return ChartRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): ChartService {
        return AppRetrofit.chartService
    }

}