package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.remote.ChecklistRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.remote.ChecklistRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.repository.ChecklistRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.repository.ChecklistRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.service.ChecklistService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChecklistModule {
    @Provides
    @Singleton
    fun provideRepository(checklistRemoteDataSource: ChecklistRemoteDataSource): ChecklistRepository {
        return ChecklistRepositoryImpl(checklistRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(checklistService: ChecklistService): ChecklistRemoteDataSource {
        return ChecklistRemoteDataSourceImpl(checklistService)
    }

    @Provides
    @Singleton
    fun provideService(): ChecklistService {
        return AppRetrofit.checklistService
    }
}