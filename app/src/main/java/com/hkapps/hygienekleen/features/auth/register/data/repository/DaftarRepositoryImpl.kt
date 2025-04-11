package com.hkapps.hygienekleen.features.auth.register.data.repository

import com.hkapps.hygienekleen.features.auth.register.data.remote.DaftarRemoteDataSource
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
import javax.inject.Inject

class DaftarRepositoryImpl @Inject constructor(private val remoteDataSource: DaftarRemoteDataSource) :

    DaftarRepository {
    override fun getEmployeeData(
        employeeNuc: String,
        employeeNik: String
    ): Single<DataEmployeeResponseModel> {
        return remoteDataSource.getEmployeeData(employeeNuc, employeeNik)
    }

    override fun sendOtp(sendOtpRequestModel: SendOtpRequestModel): Single<SendOtpResponseModel> {
        return remoteDataSource.sendOtp(sendOtpRequestModel)
    }

    override fun resendOtp(resendOtpRequestModel: ReSendOtpRequestModel): Single<ReSendOtpResponseModel> {
        return remoteDataSource.resendOtp(resendOtpRequestModel)
    }

    override fun verifyOtpRegis(verifyOtpRequestModel: VerifyOtpRequestModel): Single<VerifyOtpResponse> {
        return remoteDataSource.verifyOtp(verifyOtpRequestModel)
    }

    override fun passwordRegis(passwordRequestModel: PasswordRequestModel): Single<PasswordResponse> {
        return remoteDataSource.passwordRegis(passwordRequestModel)
    }
}