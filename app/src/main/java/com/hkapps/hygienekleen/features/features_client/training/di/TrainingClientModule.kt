package com.hkapps.hygienekleen.features.features_client.training.di

import com.hkapps.hygienekleen.features.features_client.training.data.remote.TrainingClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.training.data.remote.TrainingClientRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.training.data.repository.TrainingClientRepository
import com.hkapps.hygienekleen.features.features_client.training.data.repository.TrainingClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.training.data.service.TrainingClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TrainingClientModule {

    @Provides
    @Singleton
    fun provideRepository (trainingClientRemoteDataSource: TrainingClientRemoteDataSource): TrainingClientRepository {
        return TrainingClientRepositoryImpl(trainingClientRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource (trainingClientService: TrainingClientService): TrainingClientRemoteDataSource {
        return TrainingClientRemoteDataSourceImpl(trainingClientService)
    }

    @Provides
    @Singleton
    fun provideTrainingService(): TrainingClientService {
        return AppRetrofit.trainingClientService
    }
}