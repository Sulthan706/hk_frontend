package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.di

import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.remote.PeriodicManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.remote.PeriodicManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.repository.PeriodicManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.repository.PeriodicManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.service.PeriodicManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PeriodicManagementModule {

    @Provides
    @Singleton
    fun provideRepository(periodicManagementDataSource: PeriodicManagementDataSource): PeriodicManagementRepository {
        return PeriodicManagementRepositoryImpl(periodicManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(periodicManagementService: PeriodicManagementService): PeriodicManagementDataSource {
        return PeriodicManagementDataSourceImpl(periodicManagementService)
    }

    @Provides
    @Singleton
    fun providePeriodicService(): PeriodicManagementService {
        return AppRetrofit.periodicManagementService
    }
}