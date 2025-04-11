package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.di

import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.remote.CftakManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.remote.CftalkManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.repository.CftalkManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.repository.CftalkManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.service.CftalkManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CftalkManagementModule {

    @Provides
    @Singleton
    fun provideRepository(cftakManagementDataSource: CftakManagementDataSource): CftalkManagementRepository {
        return CftalkManagementRepositoryImpl(cftakManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(cftalkManagementService: CftalkManagementService): CftakManagementDataSource {
        return CftalkManagementDataSourceImpl(cftalkManagementService)
    }

    @Provides
    @Singleton
    fun provideCftalkManagementService(): CftalkManagementService {
        return AppRetrofit.cftalkManagementService
    }

}