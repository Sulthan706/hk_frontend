package com.hkapps.hygienekleen.features.features_vendor.service.resign.di

import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.remote.ResignRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.remote.ResignRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.repository.ResignRepository
import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.repository.ResignRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.service.ResignService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResignModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: ResignRemoteDataSource): ResignRepository {
        return ResignRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: ResignService): ResignRemoteDataSource {
        return ResignRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): ResignService {
        return AppRetrofit.resignService
    }

}