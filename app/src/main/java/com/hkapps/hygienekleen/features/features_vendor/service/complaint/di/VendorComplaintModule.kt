package com.hkapps.hygienekleen.features.features_vendor.service.complaint.di

import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.remote.VendorComplaintRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.remote.VendorComplaintRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.repository.VendorComplaintRepository
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.repository.VendorComplaintRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.service.VendorComplaintService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class VendorComplaintModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: VendorComplaintRemoteDataSource): VendorComplaintRepository {
        return VendorComplaintRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(complaintService: VendorComplaintService): VendorComplaintRemoteDataSource {
        return VendorComplaintRemoteDataSourceImpl(complaintService)
    }

    @Provides
    @Singleton
    fun provideClientComplaintService(): VendorComplaintService {
        return AppRetrofit.vendorComplaintService
    }
}