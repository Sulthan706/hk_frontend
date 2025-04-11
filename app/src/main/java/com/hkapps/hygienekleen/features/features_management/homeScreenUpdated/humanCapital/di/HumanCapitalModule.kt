package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.di

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.remote.HumanCapitalRemote
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.remote.HumanCapitalRemoteImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.repository.HumanCapitalRepoImpl
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.repository.HumanCapitalRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.service.HumanCapitalService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HumanCapitalModule {

    @Provides
    @Singleton
    fun provideRepository(humanCapitalRemote: HumanCapitalRemote): HumanCapitalRepository {
        return HumanCapitalRepoImpl(humanCapitalRemote)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(humanCapitalService: HumanCapitalService): HumanCapitalRemote {
        return HumanCapitalRemoteImpl(humanCapitalService)
    }

    @Provides
    @Singleton
    fun provideHumanCapitalService(): HumanCapitalService {
        return AppRetrofit.humanCapitalService
    }

}