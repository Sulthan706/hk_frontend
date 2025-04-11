package com.hkapps.hygienekleen.features.features_management.damagereport.di

import com.hkapps.hygienekleen.features.features_management.damagereport.data.remote.DamageReportManagementDataSource
import com.hkapps.hygienekleen.features.features_management.damagereport.data.remote.DamageReportManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.damagereport.data.repository.DamageReportManagementRepository
import com.hkapps.hygienekleen.features.features_management.damagereport.data.repository.DamageReportManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.damagereport.data.service.DamageReportManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DamageReportManagementModule {

    @Provides
    @Singleton
    fun provideRepository(damageReportManagementDataSource: DamageReportManagementDataSource): DamageReportManagementRepository{
        return DamageReportManagementRepositoryImpl(damageReportManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(damageReportManagementService: DamageReportManagementService): DamageReportManagementDataSource{
        return DamageReportManagementDataSourceImpl(damageReportManagementService)
    }

    @Provides
    @Singleton
    fun provideDamageRemportManagementService(): DamageReportManagementService {
        return AppRetrofit.damageReportManagementService
    }

}