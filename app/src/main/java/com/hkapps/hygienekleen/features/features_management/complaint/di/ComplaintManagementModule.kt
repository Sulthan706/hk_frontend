package com.hkapps.hygienekleen.features.features_management.complaint.di

import com.hkapps.hygienekleen.features.features_management.complaint.data.remote.ComplaintManagementDataSource
import com.hkapps.hygienekleen.features.features_management.complaint.data.remote.ComplaintManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.complaint.data.repository.ComplaintManagementRepository
import com.hkapps.hygienekleen.features.features_management.complaint.data.repository.ComplaintManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.complaint.data.service.ComplaintManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ComplaintManagementModule {

    @Provides
    @Singleton
    fun provideRepository(complaintManagementDataSource: ComplaintManagementDataSource): ComplaintManagementRepository {
        return ComplaintManagementRepositoryImpl(complaintManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(complaintManagementService: ComplaintManagementService): ComplaintManagementDataSource {
        return ComplaintManagementDataSourceImpl(complaintManagementService)
    }

    @Provides
    @Singleton
    fun provideComplaintManagementService(): ComplaintManagementService {
        return AppRetrofit.complaintManagementService
    }
}
