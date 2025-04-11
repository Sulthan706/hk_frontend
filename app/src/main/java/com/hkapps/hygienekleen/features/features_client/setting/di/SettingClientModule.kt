package com.hkapps.hygienekleen.features.features_client.setting.di

import com.hkapps.hygienekleen.features.features_client.setting.data.remote.SettingClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.setting.data.remote.SettingClientRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.setting.data.repository.SettingClientRepository
import com.hkapps.hygienekleen.features.features_client.setting.data.repository.SettingClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.setting.data.service.SettingClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingClientModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: SettingClientRemoteDataSource): SettingClientRepository {
        return SettingClientRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: SettingClientService): SettingClientRemoteDataSource {
        return SettingClientRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): SettingClientService{
        return AppRetrofit.settingClientService
    }
}