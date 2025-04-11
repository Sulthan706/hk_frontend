package com.hkapps.academy.features.features_trainer.homescreen.home.di

import com.hkapps.academy.features.features_trainer.homescreen.home.data.remote.HomeTrainerDataSource
import com.hkapps.academy.features.features_trainer.homescreen.home.data.remote.HomeTrainerDataSourceImpl
import com.hkapps.academy.features.features_trainer.homescreen.home.data.repository.HomeTrainerRepository
import com.hkapps.academy.features.features_trainer.homescreen.home.data.repository.HomeTrainerRepositoryImpl
import com.hkapps.academy.features.features_trainer.homescreen.home.data.service.HomeTrainerService
import com.hkapps.academy.retrofit.AcademyRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeTrainerModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: HomeTrainerDataSource): HomeTrainerRepository {
        return HomeTrainerRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: HomeTrainerService): HomeTrainerDataSource {
        return HomeTrainerDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideHomeTrainerService(): HomeTrainerService {
        return AcademyRetrofit.homeTrainerService
    }

}