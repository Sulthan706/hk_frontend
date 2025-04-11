package com.hkapps.academy.features.features_participants.homescreen.home.di

import com.hkapps.academy.features.features_participants.homescreen.home.data.remote.HomeParticipantDataSource
import com.hkapps.academy.features.features_participants.homescreen.home.data.remote.HomeParticipantDataSourceImpl
import com.hkapps.academy.features.features_participants.homescreen.home.data.repository.HomeParticipantRepository
import com.hkapps.academy.features.features_participants.homescreen.home.data.repository.HomeParticipantRepositoryImpl
import com.hkapps.academy.features.features_participants.homescreen.home.data.service.HomeParticipantService
import com.hkapps.academy.retrofit.AcademyRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeParticipantModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: HomeParticipantDataSource): HomeParticipantRepository {
        return HomeParticipantRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: HomeParticipantService): HomeParticipantDataSource {
        return HomeParticipantDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideHomeParticipantService(): HomeParticipantService {
        return AcademyRetrofit.homeParticipantService
    }

}