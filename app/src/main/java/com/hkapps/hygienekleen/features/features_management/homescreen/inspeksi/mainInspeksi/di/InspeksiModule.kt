package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.di

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.remote.InspeksiDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.remote.InspeksiDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.repository.InspeksiRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.repository.InspeksiRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.service.InspeksiService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InspeksiModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: InspeksiDataSource): InspeksiRepository {
        return InspeksiRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: InspeksiService): InspeksiDataSource {
        return InspeksiDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideInspeksiService(): InspeksiService {
        return AppRetrofit.inspeksiService
    }
}