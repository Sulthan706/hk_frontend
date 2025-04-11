package com.hkapps.hygienekleen.features.auth.login.di

import com.hkapps.hygienekleen.features.auth.login.data.remote.LoginRemoteDataSource
import com.hkapps.hygienekleen.features.auth.login.data.remote.LoginRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.auth.login.data.repository.LoginRepository
import com.hkapps.hygienekleen.features.auth.login.data.repository.LoginRepositoryImpl
import com.hkapps.hygienekleen.features.auth.login.data.service.LoginService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule {
    @Provides
    @Singleton
    fun provideRepository(loginRemoteDataSource: LoginRemoteDataSource): LoginRepository {
        return LoginRepositoryImpl(loginRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(loginService: LoginService): LoginRemoteDataSource{
        return LoginRemoteDataSourceImpl(loginService)
    }

    @Provides
    @Singleton
    fun provideLoginService(): LoginService{
        return AppRetrofit.loginService
    }
}