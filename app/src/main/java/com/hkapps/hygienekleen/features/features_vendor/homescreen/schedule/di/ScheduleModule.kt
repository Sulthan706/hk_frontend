package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.remote.ScheduleRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.remote.ScheduleRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.repository.ScheduleRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.repository.ScheduleRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.service.ScheduleService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ScheduleModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: ScheduleRemoteDataSource): ScheduleRepository {
        return ScheduleRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: ScheduleService): ScheduleRemoteDataSource {
        return ScheduleRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): ScheduleService {
        return AppRetrofit.scheduleService
    }
}