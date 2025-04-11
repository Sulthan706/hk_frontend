package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.di

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.remote.ProjectRemote
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.remote.ProjectRemoteImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.repository.ProjectRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.repository.ProjectRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.service.ProjectService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProjectModule {

    @Provides
    @Singleton
    fun provideRepository(projectRemote: ProjectRemote): ProjectRepository {
        return ProjectRepositoryImpl(projectRemote)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(projectService: ProjectService): ProjectRemote {
        return ProjectRemoteImpl(projectService)
    }

    @Provides
    @Singleton
    fun provideProjectService(): ProjectService {
        return AppRetrofit.projectService
    }

}