package com.hkapps.hygienekleen.features.auth.forgotpass.di

import com.hkapps.hygienekleen.features.auth.forgotpass.data.remote.ForgotPassRemoteDataSource
import com.hkapps.hygienekleen.features.auth.forgotpass.data.remote.ForgotPassRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.auth.forgotpass.data.repository.ForgotPassRepository
import com.hkapps.hygienekleen.features.auth.forgotpass.data.repository.ForgotPassRepositoryImpl
import com.hkapps.hygienekleen.features.auth.forgotpass.data.service.ForgotPassService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ForgotPassModule {
    @Provides
    @Singleton
    fun provideRepository(forgotRemoteDataSource: ForgotPassRemoteDataSource): ForgotPassRepository {
        return ForgotPassRepositoryImpl(forgotRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(forgotService: ForgotPassService): ForgotPassRemoteDataSource {
        return ForgotPassRemoteDataSourceImpl(forgotService)
    }

    @Provides
    @Singleton
    fun provideForgotPassService(): ForgotPassService {
        return AppRetrofit.forgotService
    }
}