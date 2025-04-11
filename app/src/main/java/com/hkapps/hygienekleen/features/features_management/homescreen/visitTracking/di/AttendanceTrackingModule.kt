package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.di

import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.remote.AttendanceTrackingDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.remote.AttendanceTrackingDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.repository.AttendanceTrackingRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.repository.AttendanceTrackingRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.service.AttendanceTrackingService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AttendanceTrackingModule {

    @Provides
    @Singleton
    fun provideRepository(attendanceTrackingDataSource: AttendanceTrackingDataSource): AttendanceTrackingRepository {
        return AttendanceTrackingRepositoryImpl(attendanceTrackingDataSource)
    }

    @Provides
    @Singleton
    fun provideDataSource(attendanceTrackingService: AttendanceTrackingService): AttendanceTrackingDataSource {
        return AttendanceTrackingDataSourceImpl(attendanceTrackingService)
    }

    @Provides
    @Singleton
    fun provideAttendanceTrackingService(): AttendanceTrackingService {
        return AppRetrofit.attendanceTrackingService
    }
}