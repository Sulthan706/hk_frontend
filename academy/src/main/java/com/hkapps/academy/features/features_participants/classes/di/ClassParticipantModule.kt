package com.hkapps.academy.features.features_participants.classes.di

import com.hkapps.academy.features.features_participants.classes.data.remote.ClassParticipantDataSource
import com.hkapps.academy.features.features_participants.classes.data.remote.ClassParticipantDataSourceImpl
import com.hkapps.academy.features.features_participants.classes.data.repository.ClassParticipantRepository
import com.hkapps.academy.features.features_participants.classes.data.repository.ClassParticipantRepositoryImpl
import com.hkapps.academy.features.features_participants.classes.data.service.ClassParticipantService
import com.hkapps.academy.retrofit.AcademyRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassParticipantModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: ClassParticipantDataSource): ClassParticipantRepository {
        return ClassParticipantRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: ClassParticipantService): ClassParticipantDataSource {
        return ClassParticipantDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideClassParticipantService(): ClassParticipantService {
        return AcademyRetrofit.classParticipantService
    }
}