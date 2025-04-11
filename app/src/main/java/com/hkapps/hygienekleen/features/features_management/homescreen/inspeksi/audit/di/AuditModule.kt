package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.di

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.remote.AuditDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.remote.AuditDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.repository.AuditRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.repository.AuditRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.service.AuditService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuditModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: AuditDataSource): AuditRepository {
        return AuditRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: AuditService): AuditDataSource {
        return  AuditDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideAuditService(): AuditService {
        return AppRetrofit.auditService
    }
}