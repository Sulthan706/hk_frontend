package com.hkapps.hygienekleen.features.features_client.report.di

import com.hkapps.hygienekleen.features.features_client.report.data.remote.ReportClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.report.data.remote.ReportClientRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.report.data.repository.ReportClientRepository
import com.hkapps.hygienekleen.features.features_client.report.data.repository.ReportClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.report.data.service.ReportClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReportClientModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: ReportClientRemoteDataSource): ReportClientRepository {
        return ReportClientRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(reportClientService: ReportClientService): ReportClientRemoteDataSource {
        return ReportClientRemoteDataSourceImpl(reportClientService)
    }

    @Provides
    @Singleton
    fun provideReportClientService(): ReportClientService {
        return AppRetrofit.reportClientService
    }
}