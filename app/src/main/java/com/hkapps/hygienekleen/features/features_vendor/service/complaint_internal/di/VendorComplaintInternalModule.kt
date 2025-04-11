package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.di

import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.remote.VendorComplaintInternalRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.remote.VendorComplaintInternalRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.repository.VendorComplaintInternalRepository
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.repository.VendorComplaintInternalRepositoryimpl
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.service.VendorComplaintInternalService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VendorComplaintInternalModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: VendorComplaintInternalRemoteDataSource): VendorComplaintInternalRepository {
        return VendorComplaintInternalRepositoryimpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(complaintInternalService: VendorComplaintInternalService): VendorComplaintInternalRemoteDataSource {
        return VendorComplaintInternalRemoteDataSourceImpl(complaintInternalService)
    }

    @Provides
    @Singleton
    fun provideComplaintInternalService(): VendorComplaintInternalService {
        return AppRetrofit.vendorComplaintInternalService
    }
}