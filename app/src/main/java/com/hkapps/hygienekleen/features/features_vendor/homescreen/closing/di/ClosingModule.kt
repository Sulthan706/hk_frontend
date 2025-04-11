package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.remote.ClosingRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.remote.ClosingRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.repository.ClosingRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.repository.ClosingRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.service.ClosingService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClosingModule {

    @Provides
    @Singleton
    fun provideRepository(closingRemoteDataSource: ClosingRemoteDataSource) : ClosingRepository{
        return ClosingRepositoryImpl(
            closingRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(closingService: ClosingService) : ClosingRemoteDataSource{
        return ClosingRemoteDataSourceImpl(
            closingService
        )
    }

    @Provides
    @Singleton
    fun provideService() : ClosingService{
        return AppRetrofit.closingService
    }
}