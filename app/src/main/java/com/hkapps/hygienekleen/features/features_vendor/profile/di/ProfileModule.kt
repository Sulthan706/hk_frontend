package com.hkapps.hygienekleen.features.features_vendor.profile.di

import com.hkapps.hygienekleen.features.features_vendor.profile.data.remote.ProfileRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.profile.data.remote.ProfileRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.profile.data.repository.ProfileRepository
import com.hkapps.hygienekleen.features.features_vendor.profile.data.repository.ProfileRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.profile.data.service.ProfileService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProfileModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: ProfileRemoteDataSource): ProfileRepository {
        return ProfileRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: ProfileService): ProfileRemoteDataSource {
        return ProfileRemoteDataSourceImpl(
            service
        )
    }

    @Provides
    @Singleton
    fun provideService(): ProfileService {
        return AppRetrofit.profileService
    }
}