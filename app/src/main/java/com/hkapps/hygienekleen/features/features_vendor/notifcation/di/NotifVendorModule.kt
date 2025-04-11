package com.hkapps.hygienekleen.features.features_vendor.notifcation.di


import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.remote.NotifRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.remote.NotifRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.repository.NotifRepository
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.repository.NotifRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.service.NotifVendorService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotifVendorModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: NotifRemoteDataSource): NotifRepository {
        return NotifRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(complaintVendorService: NotifVendorService): NotifRemoteDataSource {
        return NotifRemoteDataSourceImpl(complaintVendorService)
    }

    @Provides
    @Singleton
    fun provideNotifService(): NotifVendorService {
        return AppRetrofit.notifVendorService
    }
}