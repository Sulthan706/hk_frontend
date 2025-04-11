package com.hkapps.hygienekleen.features.features_vendor.service.overtime.di

import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.remote.OvertimeRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.remote.OvertimeRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.repository.OvertimeRepository
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.repository.OvertimeRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.service.OvertimeService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OvertimeModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: OvertimeRemoteDataSource): OvertimeRepository {
        return OvertimeRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: OvertimeService): OvertimeRemoteDataSource {
        return OvertimeRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): OvertimeService {
        return AppRetrofit.overtimeService
    }
}