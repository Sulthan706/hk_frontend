package com.hkapps.hygienekleen.features.features_client.home.di

import com.hkapps.hygienekleen.features.features_client.home.data.remote.HomeClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.home.data.remote.HomeClientRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.home.data.repository.HomeClientRepository
import com.hkapps.hygienekleen.features.features_client.home.data.repository.HomeClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.home.data.service.HomeClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeClientModule {

    @Provides
    @Singleton
    fun provideRepository(homeClientRemoteDataSource: HomeClientRemoteDataSource): HomeClientRepository {
        return HomeClientRepositoryImpl(homeClientRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(homeClientService: HomeClientService): HomeClientRemoteDataSource {
        return HomeClientRemoteDataSourceImpl(homeClientService)
    }

    @Provides
    @Singleton
    fun provideHomeClientService(): HomeClientService {
        return AppRetrofit.homeClientService
    }

}