package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.di

import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.remote.ReportHighDataSource
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.remote.ReportHighDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.repository.ReportHighRepository
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.repository.ReportHighRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.service.ReportHighService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReportHighModule {

    @Provides
    @Singleton
    fun provideRepository(reportHighDataSource: ReportHighDataSource): ReportHighRepository {
        return ReportHighRepositoryImpl(reportHighDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(reportHighService: ReportHighService): ReportHighDataSource {
        return ReportHighDataSourceImpl(reportHighService)
    }

    @Provides
    @Singleton
    fun provideReportHighService(): ReportHighService {
        return AppRetrofit.reportHighService
    }

}