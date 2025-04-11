package com.hkapps.academy.features.authentication.data.repository

import com.hkapps.academy.features.authentication.model.login.LoginAcademyResponse
import io.reactivex.Single

interface AuthAcademyRepository {

    fun loginAcademy(
        userNuc: String
    ): Single<LoginAcademyResponse>

}