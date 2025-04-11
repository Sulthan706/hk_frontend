package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.di

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.remote.ScheduleManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.remote.ScheduleManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.repository.ScheduleManagementRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.repository.ScheduleManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.service.ScheduleManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ScheduleManagementModule {

    @Provides
    @Singleton
    fun provideRepository(scheduleManagementDataSource: ScheduleManagementDataSource): ScheduleManagementRepository {
        return ScheduleManagementRepositoryImpl(scheduleManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(scheduleManagementService: ScheduleManagementService): ScheduleManagementDataSource {
        return ScheduleManagementDataSourceImpl(scheduleManagementService)
    }

    @Provides
    @Singleton
    fun provideScheduleManagementService(): ScheduleManagementService {
        return AppRetrofit.scheduleManagementService
    }

}