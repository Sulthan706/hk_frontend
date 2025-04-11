package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.di

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.remote.AbsentOprMgmntRemoteDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.remote.AbsentOprMgmntRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.repository.AbsentOprMgmntRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.repository.AbsentOprMgmntRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.service.AbsentOprMgmntService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AbsentOprManagementModule {

    @Provides
    @Singleton
    fun provideRepository(absentOprMgmntRemoteDataSource: AbsentOprMgmntRemoteDataSource): AbsentOprMgmntRepository {
        return AbsentOprMgmntRepositoryImpl(absentOprMgmntRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(absentOprMgmntService: AbsentOprMgmntService): AbsentOprMgmntRemoteDataSource {
        return AbsentOprMgmntRemoteDataSourceImpl(absentOprMgmntService)
    }

    @Provides
    @Singleton
    fun provideAbsentOprMgmntService(): AbsentOprMgmntService {
        return AppRetrofit.absentOprMgmntService
    }
}