package com.hkapps.academy.features.authentication.di

import com.hkapps.academy.features.authentication.data.remote.AuthAcademyDataSource
import com.hkapps.academy.features.authentication.data.remote.AuthAcademyDataSourceImpl
import com.hkapps.academy.features.authentication.data.repository.AuthAcademyRepository
import com.hkapps.academy.features.authentication.data.repository.AuthAcademyRepositoryImpl
import com.hkapps.academy.features.authentication.data.service.AuthAcademyService
import com.hkapps.academy.retrofit.AcademyRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthAcademyModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: AuthAcademyDataSource): AuthAcademyRepository {
        return AuthAcademyRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: AuthAcademyService): AuthAcademyDataSource {
        return AuthAcademyDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideAuthAcademyService(): AuthAcademyService {
        return AcademyRetrofit.authAcademyService
    }
}