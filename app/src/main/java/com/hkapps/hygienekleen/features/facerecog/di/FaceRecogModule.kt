package com.hkapps.hygienekleen.features.facerecog.di

import com.hkapps.hygienekleen.features.facerecog.data.remote.FaceRecogRemoteDataSource
import com.hkapps.hygienekleen.features.facerecog.data.remote.FaceRecogRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.facerecog.data.repository.FaceRecogRepository
import com.hkapps.hygienekleen.features.facerecog.data.repository.FaceRecogRepositoryImpl
import com.hkapps.hygienekleen.features.facerecog.data.service.FaceRecogService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FaceRecogModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: FaceRecogRemoteDataSource): FaceRecogRepository {
        return FaceRecogRepositoryImpl(
            remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: FaceRecogService): FaceRecogRemoteDataSource {
        return  FaceRecogRemoteDataSourceImpl(
            service
        )
    }

    @Provides
    @Singleton
    fun provideService(): FaceRecogService {
        return AppRetrofit.faceRecogService
    }
}