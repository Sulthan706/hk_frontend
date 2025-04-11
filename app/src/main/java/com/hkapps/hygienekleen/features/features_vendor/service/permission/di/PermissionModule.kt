package com.hkapps.hygienekleen.features.features_vendor.service.permission.di


import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.remote.PermissionRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.remote.PermissionRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.repository.PermissionRepository
import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.repository.PermissionRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.service.PermissionService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PermissionModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: PermissionRemoteDataSource): PermissionRepository {
        return PermissionRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: PermissionService): PermissionRemoteDataSource {
        return PermissionRemoteDataSourceImpl(
            service
        )
    }

    @Provides
    @Singleton
    fun provideService(): PermissionService {
        return AppRetrofit.permissionService
    }
}