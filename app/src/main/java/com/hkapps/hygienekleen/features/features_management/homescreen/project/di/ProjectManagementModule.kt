package com.hkapps.hygienekleen.features.features_management.homescreen.project.di

import com.hkapps.hygienekleen.features.features_management.homescreen.project.data.remote.ProjectManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.project.data.remote.ProjectManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.project.data.repository.ProjectManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.project.data.repository.ProjectManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.project.data.service.ProjectManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProjectManagementModule {

    @Provides
    @Singleton
    fun provideRepository(projectManagementDataSource: ProjectManagementDataSource): ProjectManagementRepository {
        return ProjectManagementRepositoryImpl(projectManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(projectManagementService: ProjectManagementService): ProjectManagementDataSource {
        return ProjectManagementDataSourceImpl(projectManagementService)
    }

    @Provides
    @Singleton
    fun provideProjectService(): ProjectManagementService {
        return AppRetrofit.projectManagementService
    }
}