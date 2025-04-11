package com.hkapps.academy.features.authentication.data.repository

import com.hkapps.academy.features.authentication.data.remote.AuthAcademyDataSource
import com.hkapps.academy.features.authentication.model.login.LoginAcademyResponse
import io.reactivex.Single
import javax.inject.Inject

class AuthAcademyRepositoryImpl @Inject constructor(private val dataSource: AuthAcademyDataSource):
    AuthAcademyRepository {

    override fun loginAcademy(userNuc: String): Single<LoginAcademyResponse> {
        return dataSource.loginAcademy(userNuc)
    }

}