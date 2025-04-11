package com.hkapps.hygienekleen.features.auth.login.data.service

import com.hkapps.hygienekleen.features.auth.login.model.LoginClientModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginManagementModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginRequestModel
import com.hkapps.hygienekleen.features.auth.login.model.validateimei.ValidateImeiResponseModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface LoginService {
    @POST("api/v1/employee/login")
    fun login(@Body loginRequestModel: LoginRequestModel): Single<LoginModel>
    @POST("api/v1/client/login")
    fun loginClient(@Body loginRequestModel: LoginRequestModel): Single<LoginClientModel>
    @POST("api/v1/management/login")
    fun loginManagement(@Body loginRequestModel: LoginRequestModel): Single<LoginManagementModel>

    @PUT("api/v1/phoneemployee/create")
    fun validateImei(
        @Query("employeeId") employeeId:String,
        @Query("phoneId") phoneId: String,
        @Query("phoneModel") phoneModel: String
    ): Single<ValidateImeiResponseModel>
}