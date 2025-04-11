package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.di

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.remote.VisitReportManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.remote.VisitReportManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.repository.VisitReportManagementRepoImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.repository.VisitReportManagementRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.service.VisitReportManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VisitReportManagementModule {

    @Provides
    @Singleton
    fun provideRepository(visitReportManagementDataSource: VisitReportManagementDataSource): VisitReportManagementRepository {
        return VisitReportManagementRepoImpl(visitReportManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(visitReportManagementService: VisitReportManagementService): VisitReportManagementDataSource {
        return VisitReportManagementDataSourceImpl(visitReportManagementService)
    }

    @Provides
    @Singleton
    fun provideVisitReportManagementService(): VisitReportManagementService {
        return AppRetrofit.visitReportManagementService
    }

}