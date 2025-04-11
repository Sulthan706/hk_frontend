package com.hkapps.hygienekleen.features.features_vendor.myteam.di

import com.hkapps.hygienekleen.features.features_vendor.myteam.data.remote.TimkuRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.myteam.data.remote.TimkuRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.myteam.data.repository.TimkuRepository
import com.hkapps.hygienekleen.features.features_vendor.myteam.data.repository.TimkuRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.myteam.data.service.TimkuService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TimkuModule {

    @Provides
    @Singleton
    fun provideRepository(timkuRemoteDataSource: TimkuRemoteDataSource): TimkuRepository {
        return TimkuRepositoryImpl(timkuRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(timkuService: TimkuService): TimkuRemoteDataSource {
        return TimkuRemoteDataSourceImpl(timkuService)
    }

    @Provides
    @Singleton
    fun provideTimkuService(): TimkuService {
        return AppRetrofit.timkuService
    }
}