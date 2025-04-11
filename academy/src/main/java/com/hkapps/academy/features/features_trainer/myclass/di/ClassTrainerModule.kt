package com.hkapps.academy.features.features_trainer.myclass.di

import com.hkapps.academy.features.features_trainer.myclass.data.remote.ClassTrainerDataSource
import com.hkapps.academy.features.features_trainer.myclass.data.remote.ClassTrainerDataSourceImpl
import com.hkapps.academy.features.features_trainer.myclass.data.repository.ClassTrainerRepository
import com.hkapps.academy.features.features_trainer.myclass.data.repository.ClassTrainerRepositoryImpl
import com.hkapps.academy.features.features_trainer.myclass.data.service.ClassTrainerService
import com.hkapps.academy.retrofit.AcademyRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassTrainerModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: ClassTrainerDataSource): ClassTrainerRepository {
        return ClassTrainerRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: ClassTrainerService): ClassTrainerDataSource {
        return ClassTrainerDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideClassTrainerService(): ClassTrainerService {
        return AcademyRetrofit.classTrainerService
    }
}