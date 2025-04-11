package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.di

import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.remote.MonthlyWorkClientDataSource
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.remote.MonthlyWorkClientDataSourceImpl
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.repository.MonthlyWorkClientRepository
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.repository.MonthlyWorkClientRepositoryImpl
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.service.MonthlyWorkClientService
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MonthlyWorkClientModule {

    @Provides
    @Singleton
    fun provideRepository(monthlyWorkClientDataSource: MonthlyWorkClientDataSource): MonthlyWorkClientRepository {
        return MonthlyWorkClientRepositoryImpl(monthlyWorkClientDataSource)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(monthlyWorkClientService: MonthlyWorkClientService): MonthlyWorkClientDataSource {
        return MonthlyWorkClientDataSourceImpl(monthlyWorkClientService)
    }

    @Provides
    @Singleton
    fun provideMonthlyWorkClientService(): MonthlyWorkClientService {
        return AppRetrofit.monthlWorkClientService
    }

}