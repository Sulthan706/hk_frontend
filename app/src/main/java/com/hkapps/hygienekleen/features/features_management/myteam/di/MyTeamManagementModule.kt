package com.hkapps.hygienekleen.features.features_management.myteam.di

import com.hkapps.hygienekleen.features.features_management.myteam.data.remote.MyTeamManagementDataSource
import com.hkapps.hygienekleen.features.features_management.myteam.data.remote.MyTeamManagementDataSourceImpl
import com.hkapps.hygienekleen.features.features_management.myteam.data.repository.MyTeamManagementRepository
import com.hkapps.hygienekleen.features.features_management.myteam.data.repository.MyTeamManagementRepositoryImpl
import com.hkapps.hygienekleen.features.features_management.myteam.data.service.MyTeamManagementService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MyTeamManagementModule {

    @Provides
    @Singleton
    fun provideRepository(myTeamManagementDataSource: MyTeamManagementDataSource): MyTeamManagementRepository {
        return MyTeamManagementRepositoryImpl(myTeamManagementDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(myTeamManagementService: MyTeamManagementService): MyTeamManagementDataSource {
        return MyTeamManagementDataSourceImpl(myTeamManagementService)
    }

    @Provides
    @Singleton
    fun provideMyTeamService(): MyTeamManagementService {
        return AppRetrofit.myTeamManagementService
    }
}