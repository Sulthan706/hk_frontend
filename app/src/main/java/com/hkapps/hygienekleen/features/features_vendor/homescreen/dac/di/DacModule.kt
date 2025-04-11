package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.remote.DacRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.remote.DacRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.repository.DacRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.repository.DacRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.service.DacService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DacModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: DacRemoteDataSource): DacRepository {
        return DacRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: DacService): DacRemoteDataSource {
        return DacRemoteDataSourceImpl(
            service
        )
    }

    @Provides
    @Singleton
    fun provideService(): DacService {
        return AppRetrofit.dacService
    }
}