package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.remote.ReportDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.remote.ReportDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.repository.ReportRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.repository.ReportRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.service.ReportService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReportModule {

    @Provides
    @Singleton
    fun provideRepository(reportDataSource: ReportDataSource): ReportRepository {
        return ReportRepositoryImpl(reportDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(reportService: ReportService): ReportDataSource {
        return ReportDataSourceImpl(reportService)
    }

    @Provides
    @Singleton
    fun provideService(): ReportService {
        return AppRetrofit.reportService
    }
}