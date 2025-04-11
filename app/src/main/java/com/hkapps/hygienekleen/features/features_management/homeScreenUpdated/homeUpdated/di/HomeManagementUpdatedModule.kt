package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.di

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.remote.HomeManagementUpdatedRemote
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.remote.HomeManagementUpdatedRemoteImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.repository.HomeManagementUpdateRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.repository.HomeManagementUpdatedRepoImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.service.HomeManagementUpdatedService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeManagementUpdatedModule {
    @Provides
    @Singleton
    fun provideRepository(homeManagementUpdatedRemote: HomeManagementUpdatedRemote): HomeManagementUpdateRepository {
        return HomeManagementUpdatedRepoImpl(homeManagementUpdatedRemote)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(homeManagementUpdatedService: HomeManagementUpdatedService): HomeManagementUpdatedRemote {
        return HomeManagementUpdatedRemoteImpl(homeManagementUpdatedService)
    }

    @Provides
    @Singleton
    fun provideHomeManagementUpdatedService(): HomeManagementUpdatedService {
        return AppRetrofit.homeManagementUpdatedService
    }

}