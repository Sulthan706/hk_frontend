package com.hkapps.hygienekleen.features.auth.forgotpass.data.service

import com.hkapps.hygienekleen.features.auth.forgotpass.model.*
import io.reactivex.Single
import retrofit2.http.*

interface ForgotPassService {

    //send otp ke email
    @POST("api/v1/employee/send-otp")
    fun forgot(@Body forgotRequestModel: ForgotRequestModel): Single<ForgotModel>

    //get otp
//    @Headers("Content-Type: application/json")
    @POST("api/v1/employee/verify-otp")
    fun forgotOTP(
        @Body forgotOTPRequestModel: ForgotOTPRequestModel): Single<ForgotOTPResponseModel>


    //put otp
    @PUT("api/v1/employee/change-password")
    fun forgotPassChangePass(
        @Body forgotPassChangePassRequestModel: ForgotPassChangePassRequestModel
    ): Single<ForgotPassChangePassResponseModel>
}