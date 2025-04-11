package com.hkapps.hygienekleen.features.features_management.service.overtime.di

import com.hkapps.hygienekleen.features.features_management.service.overtime.data.remote.OvertimeManagementDataSource
import com.hkapps.hygienekleen.features.features_management.service.overtime.data.remote.OvertimeManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.service.overtime.data.repository.OvertimeManagementRepository
import com.hkapps.hygienekleen.features.features_management.service.overtime.data.repository.OvertimeManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.service.overtime.data.service.OvertimeManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OvertimeManagementModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: OvertimeManagementDataSource): OvertimeManagementRepository {
        return OvertimeManagementRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: OvertimeManagementService): OvertimeManagementDataSource {
        return OvertimeManagementDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): OvertimeManagementService {
        return AppRetrofit.overtimeManagementService
    }
}