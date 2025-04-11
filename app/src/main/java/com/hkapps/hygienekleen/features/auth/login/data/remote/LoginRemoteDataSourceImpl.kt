package com.hkapps.hygienekleen.features.auth.login.data.remote


import com.hkapps.hygienekleen.features.auth.login.data.service.LoginService
import com.hkapps.hygienekleen.features.auth.login.model.LoginClientModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginManagementModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginRequestModel
import com.hkapps.hygienekleen.features.auth.login.model.validateimei.ValidateImeiResponseModel
import io.reactivex.Single
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(private val service: LoginService): LoginRemoteDataSource {
    override fun login(loginRequestModel: LoginRequestModel): Single<LoginModel> {
        return service.login(loginRequestModel)
    }
    override fun loginClient(loginRequestModel: LoginRequestModel): Single<LoginClientModel> {
        return service.loginClient(loginRequestModel)
    }

    override fun loginManagement(loginRequestModel: LoginRequestModel): Single<LoginManagementModel> {
        return service.loginManagement(loginRequestModel)
    }

    override fun validateImei(
        employeeId: String,
        phoneId: String,
        phoneModel: String
    ): Single<ValidateImeiResponseModel> {
        return service.validateImei(employeeId, phoneId, phoneModel)
    }
}