package com.hkapps.hygienekleen.features.auth.register.data.remote

import com.hkapps.hygienekleen.features.auth.register.data.service.DaftarService
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

class DaftarRemoteDataSourceImpl @Inject constructor(private val service: DaftarService) :

    DaftarRemoteDataSource {
    override fun getEmployeeData(
        employeeNuc: String,
        employeeNik: String
    ): Single<DataEmployeeResponseModel> {
        return service.getEmployeeData(employeeNuc, employeeNik)
    }

    override fun sendOtp(sendOtpRequestModel: SendOtpRequestModel): Single<SendOtpResponseModel> {
        return service.sendOtpRegis(sendOtpRequestModel)
    }

    override fun resendOtp(resendOtpRequestModel: ReSendOtpRequestModel): Single<ReSendOtpResponseModel> {
        return service.resendOtpRegis(resendOtpRequestModel)
    }


    override fun verifyOtp(verifyOtpRequestModel: VerifyOtpRequestModel): Single<VerifyOtpResponse> {
        return service.verifyOtpRegis(verifyOtpRequestModel)
    }

    override fun passwordRegis(passwordRequestModel: PasswordRequestModel): Single<PasswordResponse> {
        return service.passwordRegis(passwordRequestModel)
    }
}