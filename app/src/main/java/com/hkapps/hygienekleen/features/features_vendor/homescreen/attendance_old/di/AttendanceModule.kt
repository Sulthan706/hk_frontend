package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.di

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.remote.AttendanceRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.remote.AttendanceRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.repository.AttendanceRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.repository.AttendanceRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.service.AttendanceService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AttendanceModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: AttendanceRemoteDataSource): AttendanceRepository {
        return AttendanceRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: AttendanceService): AttendanceRemoteDataSource {
        return AttendanceRemoteDataSourceImpl(
            service
        )
    }

    @Provides
    @Singleton
    fun provideService(): AttendanceService {
        return AppRetrofit.attendanceService
    }
}