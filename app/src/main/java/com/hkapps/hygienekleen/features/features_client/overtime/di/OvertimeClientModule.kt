package com.hkapps.hygienekleen.features.features_client.overtime.di

import com.hkapps.hygienekleen.features.features_client.overtime.data.remote.OvertimeClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.overtime.data.remote.OvertimeClientRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.overtime.data.repository.OvertimeClientRepository
import com.hkapps.hygienekleen.features.features_client.overtime.data.repository.OvertimeClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.overtime.data.service.OvertimeClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OvertimeClientModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: OvertimeClientRemoteDataSource): OvertimeClientRepository {
        return OvertimeClientRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(overtimeClientService: OvertimeClientService): OvertimeClientRemoteDataSource {
        return OvertimeClientRemoteDataSourceImpl(overtimeClientService)
    }

    @Provides
    @Singleton
    fun provideOvertimeClientService(): OvertimeClientService {
        return AppRetrofit.overtimeClientService
    }
}