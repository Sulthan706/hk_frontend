package com.hkapps.hygienekleen.features.features_client.notifcation.di


import com.hkapps.hygienekleen.features.features_client.notifcation.data.remote.NotifRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.notifcation.data.remote.NotifRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.notifcation.data.repository.NotifRepository
import com.hkapps.hygienekleen.features.features_client.notifcation.data.repository.NotifRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.notifcation.data.service.NotifClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotifModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: NotifRemoteDataSource): NotifRepository {
        return NotifRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(complaintClientService: NotifClientService): NotifRemoteDataSource {
        return NotifRemoteDataSourceImpl(complaintClientService)
    }

    @Provides
    @Singleton
    fun provideNotifService(): NotifClientService {
        return AppRetrofit.NOTIF_CLIENT_SERVICE
    }
}