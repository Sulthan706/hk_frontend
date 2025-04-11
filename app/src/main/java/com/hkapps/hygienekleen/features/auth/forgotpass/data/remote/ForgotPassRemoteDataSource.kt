package com.hkapps.hygienekleen.features.auth.forgotpass.data.remote
import com.hkapps.hygienekleen.features.auth.forgotpass.model.*
import io.reactivex.Single

interface ForgotPassRemoteDataSource {
    fun forgotRDS(forgotRequestModel: ForgotRequestModel): Single<ForgotModel>
    fun forgotOTPRDS(forgotOTPRequestModel: ForgotOTPRequestModel): Single<ForgotOTPResponseModel>
    fun forgotPassChangePassRDS(forgotPassChangePassRequestModel: ForgotPassChangePassRequestModel): Single<ForgotPassChangePassResponseModel>

}