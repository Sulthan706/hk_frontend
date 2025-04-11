package com.hkapps.hygienekleen.features.auth.register.di

import com.hkapps.hygienekleen.features.auth.register.data.remote.DaftarRemoteDataSource
import com.hkapps.hygienekleen.features.auth.register.data.remote.DaftarRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.auth.register.data.repository.DaftarRepository
import com.hkapps.hygienekleen.features.auth.register.data.repository.DaftarRepositoryImpl
import com.hkapps.hygienekleen.features.auth.register.data.service.DaftarService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaftarModule {

    @Provides
    @Singleton
    fun provideRepository(daftarRemoteDataSource: DaftarRemoteDataSource): DaftarRepository {
        return DaftarRepositoryImpl(daftarRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(daftarService: DaftarService): DaftarRemoteDataSource {
        return DaftarRemoteDataSourceImpl(daftarService)
    }

    @Provides
    @Singleton
    fun provideDaftarService(): DaftarService {
        return AppRetrofit.daftarService
    }
}