package com.hkapps.hygienekleen.features.features_client.visitreport.di

import com.hkapps.hygienekleen.features.features_client.visitreport.data.remote.VisitReportRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.visitreport.data.remote.VisitReportRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.visitreport.data.repository.VisitReportRepository
import com.hkapps.hygienekleen.features.features_client.visitreport.data.repository.VisitReportRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.visitreport.data.service.VisitReportService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VisitReportModule {

    @Provides
    @Singleton
    fun provideRepository (visitReportRemoteDataSource: VisitReportRemoteDataSource): VisitReportRepository {
        return VisitReportRepositoryImpl(visitReportRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(visitReportService: VisitReportService): VisitReportRemoteDataSource{
        return VisitReportRemoteDataSourceImpl(visitReportService)
    }

    @Provides
    @Singleton
    fun provideVisitReportService(): VisitReportService{
        return AppRetrofit.visitReportService
    }

}