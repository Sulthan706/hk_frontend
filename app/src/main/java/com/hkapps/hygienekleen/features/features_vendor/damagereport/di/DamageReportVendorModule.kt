package com.hkapps.hygienekleen.features.features_vendor.damagereport.di

import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.remote.DamageReportVendorDataSource
import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.remote.DamageReportVendorDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.repository.DamageReportVendorRepository
import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.repository.DamageReportVendorRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.service.DamageReportVendorService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DamageReportVendorModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: DamageReportVendorDataSource): DamageReportVendorRepository {
        return DamageReportVendorRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: DamageReportVendorService): DamageReportVendorDataSource{
        return DamageReportVendorDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideService(): DamageReportVendorService {
        return AppRetrofit.damageReportVendorService
    }

}