package com.hkapps.hygienekleen.features.features_vendor.service.approval.di

import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.remote.ApprovalDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.remote.ApprovalDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.repository.ApprovalRepository
import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.repository.ApprovalRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.service.ApprovalService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApprovalModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: ApprovalDataSource): ApprovalRepository {
        return ApprovalRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(approvalService: ApprovalService) : ApprovalDataSource {
        return ApprovalDataSourceImpl(approvalService)
    }

    @Provides
    @Singleton
    fun provideApprovalService(): ApprovalService {
        return AppRetrofit.approvalService
    }
}