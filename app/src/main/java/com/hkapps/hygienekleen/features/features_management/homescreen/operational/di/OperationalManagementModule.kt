package com.hkapps.hygienekleen.features.features_management.homescreen.operational.di

import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.remote.OperationalManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.remote.OperationalManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.repository.OperationalManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.repository.OperationalManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.service.OperationalManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OperationalManagementModule {

    @Provides
    @Singleton
    fun provideRepository(operationalManagementDataSource: OperationalManagementDataSource): OperationalManagementRepository {
        return OperationalManagementRepositoryImpl(operationalManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSourcee(operationalManagementService: OperationalManagementService): OperationalManagementDataSource {
        return OperationalManagementDataSourceImpl(operationalManagementService)
    }

    @Provides
    @Singleton
    fun provideOperationalManagementService(): OperationalManagementService {
        return AppRetrofit.operationalManagementService
    }

}