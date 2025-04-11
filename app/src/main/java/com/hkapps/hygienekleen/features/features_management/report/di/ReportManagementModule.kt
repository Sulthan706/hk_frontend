package com.hkapps.hygienekleen.features.features_management.report.di

import com.hkapps.hygienekleen.features.features_management.report.data.remote.ReportManagementDataSource
import com.hkapps.hygienekleen.features.features_management.report.data.remote.ReportManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.report.data.repository.ReportManagementRepository
import com.hkapps.hygienekleen.features.features_management.report.data.repository.ReportManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.report.data.service.ReportManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReportManagementModule {

    @Provides
    @Singleton
    fun provideRepository(reportManagementDataSource: ReportManagementDataSource): ReportManagementRepository {
        return ReportManagementRepositoryImpl(reportManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(reportManagementService: ReportManagementService): ReportManagementDataSource {
        return ReportManagementDataSourceImpl(reportManagementService)
    }

    @Provides
    @Singleton
    fun provideReportManagementService(): ReportManagementService{
        return AppRetrofit.reportManagementService
    }
}