package com.hkapps.hygienekleen.features.features_management.shareloc.di

import com.hkapps.hygienekleen.features.features_management.shareloc.data.remote.ShareLocManagementDataSource
import com.hkapps.hygienekleen.features.features_management.shareloc.data.remote.ShareLocManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.shareloc.data.repository.ShareLocManagementRepository
import com.hkapps.hygienekleen.features.features_management.shareloc.data.repository.ShareLocManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.shareloc.data.service.ShareLocManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShareLocManagementModule {

    @Provides
    @Singleton
    fun provideRepository(shareLocManagementDataSource: ShareLocManagementDataSource): ShareLocManagementRepository {
        return ShareLocManagementRepositoryImpl(shareLocManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(shareLocManagementService: ShareLocManagementService): ShareLocManagementDataSource {
        return ShareLocManagementDataSourceImpl(shareLocManagementService)
    }

    @Provides
    @Singleton
    fun provideShareLocManagement(): ShareLocManagementService {
        return AppRetrofit.shareLocManagementService
    }
}