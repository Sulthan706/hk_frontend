package com.hkapps.hygienekleen.features.features_management.homescreen.closing.di


import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.remote.ClosingManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.remote.ClosingManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.repository.ClosingManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.repository.ClosingManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.service.ClosingManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClosingManagementModule {

    @Provides
    @Singleton
    fun provideRepository(closingRemoteDataSource: ClosingManagementDataSource) : ClosingManagementRepository {
        return ClosingManagementRepositoryImpl(
            closingRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(closingService: ClosingManagementService) : ClosingManagementDataSource {
        return ClosingManagementDataSourceImpl(
            closingService
        )
    }

    @Provides
    @Singleton
    fun provideService() : ClosingManagementService {
        return AppRetrofit.closingManagementService
    }
}