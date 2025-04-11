package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.di

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.remote.AttendanceManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.remote.AttendanceManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.repository.AttendanceManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.repository.AttendanceManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.service.AttendanceManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AttendanceManagementModule {

    @Provides
    @Singleton
    fun provideRepository(dataSource: AttendanceManagementDataSource): AttendanceManagementRepository {
        return AttendanceManagementRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: AttendanceManagementService): AttendanceManagementDataSource {
        return AttendanceManagementDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideAttendanceManagementService(): AttendanceManagementService {
        return AppRetrofit.attendanceManagementService
    }
}