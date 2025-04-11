package com.hkapps.academy.features.authentication.data.remote

import com.hkapps.academy.features.authentication.data.service.AuthAcademyService
import com.hkapps.academy.features.authentication.model.login.LoginAcademyResponse
import io.reactivex.Single
import javax.inject.Inject

class AuthAcademyDataSourceImpl @Inject constructor(private val service: AuthAcademyService):
    AuthAcademyDataSource {

    override fun loginAcademy(userNuc: String): Single<LoginAcademyResponse> {
        return service.loginAcademy(userNuc)
    }

}