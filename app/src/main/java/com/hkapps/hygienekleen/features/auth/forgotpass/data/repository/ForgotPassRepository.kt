package com.hkapps.hygienekleen.features.auth.forgotpass.data.repository

import com.hkapps.hygienekleen.features.auth.forgotpass.model.*
import io.reactivex.Single

interface ForgotPassRepository {
    fun forgot(forgotRequestModel: ForgotRequestModel): Single<ForgotModel>
    fun forgotOTP(forgotOTPRequestModel: ForgotOTPRequestModel): Single<ForgotOTPResponseModel>
    fun forgotPassChangePass(forgotPassChangePassRequestModel: ForgotPassChangePassRequestModel): Single<ForgotPassChangePassResponseModel>
}