package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.remote.MonthlyWorkReportDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.remote.MonthlyWorkReportDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.repository.MonthlyWorkReportRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.repository.MonthlyWorkReportRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.service.MonthlyWorkReportService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MonthlyWorkReportModule {

    @Provides
    @Singleton
    fun provideRepository(monthlyWorkReportDataSource: MonthlyWorkReportDataSource): MonthlyWorkReportRepository {
        return MonthlyWorkReportRepositoryImpl(monthlyWorkReportDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(monthlyWorkReportService: MonthlyWorkReportService): MonthlyWorkReportDataSource {
        return MonthlyWorkReportDataSourceImpl(monthlyWorkReportService)
    }

    @Provides
    @Singleton
    fun provideService(): MonthlyWorkReportService{
        return AppRetrofit.monthlyWorkReportService
    }
}