package com.hkapps.academy.features.features_participants.training.di

import com.hkapps.academy.features.features_participants.training.data.remote.TrainingParticipantDataSource
import com.hkapps.academy.features.features_participants.training.data.remote.TrainingParticipantDataSourceImpl
import com.hkapps.academy.features.features_participants.training.data.repository.TrainingParticipantRepository
import com.hkapps.academy.features.features_participants.training.data.repository.TrainingParticipantRepositoryImpl
import com.hkapps.academy.features.features_participants.training.data.service.TrainingParticipantService
import com.hkapps.academy.retrofit.AcademyRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TrainingParticipantModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: TrainingParticipantDataSource): TrainingParticipantRepository {
        return TrainingParticipantRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: TrainingParticipantService): TrainingParticipantDataSource {
        return TrainingParticipantDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideTrainingParticipantService(): TrainingParticipantService {
        return AcademyRetrofit.trainingParticipantService
    }

}