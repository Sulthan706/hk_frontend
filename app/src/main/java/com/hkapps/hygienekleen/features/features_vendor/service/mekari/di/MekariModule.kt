package com.hkapps.hygienekleen.features.features_vendor.service.mekari.di

import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.remote.MekariDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.remote.MekariDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.repository.MekariRepository
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.repository.MekariRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.service.MekariService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MekariModule {
    @Provides
    @Singleton
    fun provideRepository(dataSource: MekariDataSource): MekariRepository {
        return MekariRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(mekariService: MekariService): MekariDataSource {
        return MekariDataSourceImpl(mekariService)
    }

    @Provides
    @Singleton
    fun provideMekariService(): MekariService {
        return AppRetrofit.mekariServie
    }
}