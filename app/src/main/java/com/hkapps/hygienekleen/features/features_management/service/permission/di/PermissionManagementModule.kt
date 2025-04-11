package com.hkapps.hygienekleen.features.features_management.service.permission.di

import com.hkapps.hygienekleen.features.features_management.service.permission.data.remote.PermissionManagementDataSource
import com.hkapps.hygienekleen.features.features_management.service.permission.data.remote.PermissionManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.service.permission.data.repository.PermissionManagementRepository
import com.hkapps.hygienekleen.features.features_management.service.permission.data.repository.PermissionManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.service.permission.data.service.PermissionManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PermissionManagementModule {

    @Provides
    @Singleton
    fun provideRepository(permissionManagementDataSource: PermissionManagementDataSource): PermissionManagementRepository {
        return PermissionManagementRepositoryImpl(permissionManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(permissionManagementService: PermissionManagementService): PermissionManagementDataSource {
        return PermissionManagementDataSourceImpl(permissionManagementService)
    }

    @Provides
    @Singleton
    fun providePermissionManagementService(): PermissionManagementService {
        return AppRetrofit.permissionManagementService
    }
}