package com.hkapps.hygienekleen.features.features_management.service.resign.di

import com.hkapps.hygienekleen.features.features_management.service.resign.data.remote.ResignManagementDataSource
import com.hkapps.hygienekleen.features.features_management.service.resign.data.remote.ResignManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.service.resign.data.repository.ResignManagementRepository
import com.hkapps.hygienekleen.features.features_management.service.resign.data.repository.ResignManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.service.resign.data.service.ResignManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResignManagementModule {

    @Provides
    @Singleton
    fun provideRepository(resignManagementDataSource: ResignManagementDataSource): ResignManagementRepository {
        return ResignManagementRepositoryImpl(resignManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(resignManagementService: ResignManagementService): ResignManagementDataSource {
        return ResignManagementDataSourceImpl(resignManagementService)
    }

    @Provides
    @Singleton
    fun provideResignManagementService(): ResignManagementService {
        return AppRetrofit.resignManagementService
    }

}