package com.hkapps.hygienekleen.features.auth.login.data.remote


import com.hkapps.hygienekleen.features.auth.login.model.LoginClientModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginManagementModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginRequestModel
import com.hkapps.hygienekleen.features.auth.login.model.validateimei.ValidateImeiResponseModel
import io.reactivex.Single

interface LoginRemoteDataSource {
    fun login(loginRequestModel: LoginRequestModel): Single<LoginModel>
    fun loginClient(loginRequestModel: LoginRequestModel): Single<LoginClientModel>
    fun loginManagement(loginRequestModel: LoginRequestModel): Single<LoginManagementModel>

    fun validateImei(
        employeeId:String,
        phoneId: String,
        phoneModel: String
    ): Single<ValidateImeiResponseModel>
}