package com.hkapps.hygienekleen.features.auth.forgotpass.data.remote

import com.hkapps.hygienekleen.features.auth.forgotpass.data.service.ForgotPassService
import com.hkapps.hygienekleen.features.auth.forgotpass.model.*
import io.reactivex.Single
import javax.inject.Inject

class ForgotPassRemoteDataSourceImpl @Inject constructor(private val service: ForgotPassService):
    ForgotPassRemoteDataSource {
    override fun forgotRDS(forgotRequestModel: ForgotRequestModel): Single<ForgotModel> {
        return service.forgot(forgotRequestModel)
    }

    override fun forgotOTPRDS(forgotOTPRequestModel: ForgotOTPRequestModel): Single<ForgotOTPResponseModel> {
        return service.forgotOTP(forgotOTPRequestModel)
    }


    override fun forgotPassChangePassRDS(forgotPassChangePassRequestModel: ForgotPassChangePassRequestModel): Single<ForgotPassChangePassResponseModel> {
        return service.forgotPassChangePass(forgotPassChangePassRequestModel)
    }



}