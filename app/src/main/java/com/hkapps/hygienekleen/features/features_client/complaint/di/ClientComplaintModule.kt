package com.hkapps.hygienekleen.features.features_client.complaint.di


import com.hkapps.hygienekleen.features.features_client.complaint.data.remote.ClientComplaintRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.complaint.data.remote.ClientComplaintRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.complaint.data.repository.ClientComplaintRepository
import com.hkapps.hygienekleen.features.features_client.complaint.data.repository.ClientComplaintRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.complaint.data.service.ClientComplaintService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClientComplaintModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: ClientComplaintRemoteDataSource): ClientComplaintRepository {
        return ClientComplaintRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(complaintService: ClientComplaintService): ClientComplaintRemoteDataSource {
        return ClientComplaintRemoteDataSourceImpl(complaintService)
    }

    @Provides
    @Singleton
    fun provideClientComplaintService(): ClientComplaintService {
        return AppRetrofit.clientComplaintService
    }
}