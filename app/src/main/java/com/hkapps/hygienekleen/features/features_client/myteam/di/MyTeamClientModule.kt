package com.hkapps.hygienekleen.features.features_client.myteam.di

import com.hkapps.hygienekleen.features.features_client.myteam.data.remote.MyTeamClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.myteam.data.remote.MyTeamClientRemoteDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.myteam.data.repository.MyTeamClientRepository
import com.hkapps.hygienekleen.features.features_client.myteam.data.repository.MyTeamClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.myteam.data.service.MyTeamClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MyTeamClientModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: MyTeamClientRemoteDataSource): MyTeamClientRepository {
        return MyTeamClientRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(myTeamClientService: MyTeamClientService): MyTeamClientRemoteDataSource {
        return MyTeamClientRemoteDataSourceImpl(myTeamClientService)
    }

    @Provides
    @Singleton
    fun provideMyTeamClientService(): MyTeamClientService {
        return AppRetrofit.myTeamClientService
    }

}