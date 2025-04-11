package com.hkapps.hygienekleen.features.auth.register.data.service

import com.hkapps.hygienekleen.features.auth.register.model.changePass.PasswordRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.changePass.PasswordResponse
import com.hkapps.hygienekleen.features.auth.register.model.getDataEmployee.DataEmployeeResponseModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.ReSendOtpRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.ReSendOtpResponseModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.SendOtpRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.SendOtpResponseModel
import com.hkapps.hygienekleen.features.auth.register.model.verifyOtp.VerifyOtpRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.verifyOtp.VerifyOtpResponse
import io.reactivex.Single
import retrofit2.http.*

interface DaftarService {
    @GET("api/v1/employee/{NUC}/{NIK}")
    fun getEmployeeData(
        @Path("NUC") employeeNuc: String,
        @Path("NIK") employeeNik: String
    ): Single<DataEmployeeResponseModel>

    @POST("api/v1/employee/register/send-otp")
    fun sendOtpRegis(@Body sendOtpRequestModel: SendOtpRequestModel): Single<SendOtpResponseModel>

    @POST("api/v1/employee/register/resend-otp")
    fun resendOtpRegis(@Body resendOtpRequestModel: ReSendOtpRequestModel): Single<ReSendOtpResponseModel>

    @POST("api/v1/employee/verify-otp")
    fun verifyOtpRegis(@Body verifyOtpRequestModel: VerifyOtpRequestModel): Single<VerifyOtpResponse>

    @PUT("api/v1/employee/create-password")
    fun passwordRegis(@Body passwordRequestModel: PasswordRequestModel): Single<PasswordResponse>
}