package com.hkapps.hygienekleen.features.auth.login.data.repository

import com.hkapps.hygienekleen.features.auth.login.data.remote.LoginRemoteDataSource
import com.hkapps.hygienekleen.features.auth.login.model.LoginClientModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginManagementModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginRequestModel
import com.hkapps.hygienekleen.features.auth.login.model.validateimei.ValidateImeiResponseModel
import io.reactivex.Single
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val remoteDataSource: LoginRemoteDataSource): LoginRepository {
    override fun login(loginRequestModel: LoginRequestModel): Single<LoginModel> {
        return remoteDataSource.login(loginRequestModel)
    }
    override fun loginClient(loginRequestModel: LoginRequestModel): Single<LoginClientModel> {
        return remoteDataSource.loginClient(loginRequestModel)
    }

    override fun loginManagement(loginRequestModel: LoginRequestModel): Single<LoginManagementModel> {
        return remoteDataSource.loginManagement(loginRequestModel)
    }

    override fun validateImei(
        employeeId: String,
        phoneId: String,
        phoneModel: String
    ): Single<ValidateImeiResponseModel> {
        return remoteDataSource.validateImei(employeeId, phoneId, phoneModel)
    }
}