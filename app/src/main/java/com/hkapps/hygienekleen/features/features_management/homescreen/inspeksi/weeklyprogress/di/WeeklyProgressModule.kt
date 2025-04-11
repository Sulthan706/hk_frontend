package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.di


import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.remote.WeeklyProgressDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.remote.WeeklyProgressDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.repository.WeeklyProgressRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.repository.WeeklyProgressRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.service.WeeklyProgressService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WeeklyProgressModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource : WeeklyProgressDataSource): WeeklyProgressRepository {
        return WeeklyProgressRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service : WeeklyProgressService): WeeklyProgressDataSource {
        return WeeklyProgressDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideWeeklyProgressService() : WeeklyProgressService{
        return AppRetrofit.weeklyProgressService

    }

}