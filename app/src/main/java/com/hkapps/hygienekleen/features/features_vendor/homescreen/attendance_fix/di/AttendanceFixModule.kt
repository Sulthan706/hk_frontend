package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.di
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.remote.AttendanceFixRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.remote.AttendanceFixRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.repository.AttendanceFixRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.repository.AttendanceFixRepositoryImpl
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.service.AttendanceFixService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AttendanceFixModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: AttendanceFixRemoteDataSource): AttendanceFixRepository {
        return AttendanceFixRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: AttendanceFixService): AttendanceFixRemoteDataSource {
        return AttendanceFixRemoteDataSourceImpl(
            service
        )
    }

    @Provides
    @Singleton
    fun provideService(): AttendanceFixService {
        return AppRetrofit.attendanceFixService
    }
}