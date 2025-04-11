package com.hkapps.hygienekleen.features.auth.register.data.repository

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

interface DaftarRepository {

    fun getEmployeeData(employeeNuc: String, employeeNik: String): Single<DataEmployeeResponseModel>

    fun sendOtp(sendOtpRequestModel: SendOtpRequestModel): Single<SendOtpResponseModel>

    fun resendOtp(resendOtpRequestModel: ReSendOtpRequestModel): Single<ReSendOtpResponseModel>

    fun verifyOtpRegis(verifyOtpRequestModel: VerifyOtpRequestModel): Single<VerifyOtpResponse>

    fun passwordRegis(passwordRequestModel: PasswordRequestModel): Single<PasswordResponse>
}