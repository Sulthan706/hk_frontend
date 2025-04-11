package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.di

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.remote.RoutineDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.remote.RoutineDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.repository.RoutineRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.repository.RoutineRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.service.RoutineService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoutineModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: RoutineDataSource): RoutineRepository {
        return RoutineRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: RoutineService): RoutineDataSource {
        return RoutineDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideRoutineService(): RoutineService {
        return AppRetrofit.routineService
    }
}