package com.hkapps.hygienekleen.features.features_client.dashboardProject.di

import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.remote.DashboardProjectRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.remote.DashboardProjectRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.repository.DashboardProjectRepository
import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.repository.DashboardProjectRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.service.DashboardProjectService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DashboardProjectModule {

    @Provides
    @Singleton
    fun provideRepository (remoteDataSource: DashboardProjectRemoteDataSource): DashboardProjectRepository {
        return DashboardProjectRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource (dashboardProjectService: DashboardProjectService): DashboardProjectRemoteDataSource {
        return DashboardProjectRemoteDataSourceImpl(dashboardProjectService)
    }

    @Provides
    @Singleton
    fun provideDashboardProjectService(): DashboardProjectService {
        return AppRetrofit.dashboardProjectService
    }

}