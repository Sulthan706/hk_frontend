package com.hkapps.hygienekleen.features.features_management.homescreen.home.di

import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.remote.HomeManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.remote.HomeManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.repository.HomeManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.repository.HomeManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.service.HomeManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeManagementModule {
    @Provides
    @Singleton
    fun provideRepository(homeManagementDataSource: HomeManagementDataSource): HomeManagementRepository {
        return HomeManagementRepositoryImpl(homeManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(homeManagementService: HomeManagementService): HomeManagementDataSource {
        return HomeManagementDataSourceImpl(homeManagementService)
    }

    @Provides
    @Singleton
    fun provideMyTeamService(): HomeManagementService {
        return AppRetrofit.homeManagementService
    }
}