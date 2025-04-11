package com.hkapps.hygienekleen.features.auth.forgotpass.data.repository

import com.hkapps.hygienekleen.features.auth.forgotpass.data.remote.ForgotPassRemoteDataSource
import com.hkapps.hygienekleen.features.auth.forgotpass.model.*
import io.reactivex.Single
import javax.inject.Inject

class ForgotPassRepositoryImpl @Inject constructor(private val remoteDataSource: ForgotPassRemoteDataSource) :
    ForgotPassRepository {
    override fun forgot(forgotRequestModel: ForgotRequestModel): Single<ForgotModel> {
        return remoteDataSource.forgotRDS(forgotRequestModel)
    }

    override fun forgotOTP(forgotOTPRequestModel: ForgotOTPRequestModel): Single<ForgotOTPResponseModel> {
        return remoteDataSource.forgotOTPRDS(forgotOTPRequestModel)
    }


    override fun forgotPassChangePass(forgotPassChangePassRequestModel: ForgotPassChangePassRequestModel): Single<ForgotPassChangePassResponseModel> {
        return remoteDataSource.forgotPassChangePassRDS(forgotPassChangePassRequestModel)
    }

}