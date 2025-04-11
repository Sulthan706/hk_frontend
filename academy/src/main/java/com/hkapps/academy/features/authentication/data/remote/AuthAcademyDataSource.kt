package com.hkapps.academy.features.authentication.data.remote

import com.hkapps.academy.features.authentication.model.login.LoginAcademyResponse
import io.reactivex.Single

interface AuthAcademyDataSource {

    fun loginAcademy(
        userNuc: String
    ): Single<LoginAcademyResponse>

}