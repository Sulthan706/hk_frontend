package com.hkapps.hygienekleen.features.auth.register.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.auth.register.data.repository.DaftarRepository
import com.hkapps.hygienekleen.features.auth.register.model.changePass.PasswordRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.changePass.PasswordResponse
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.ReSendOtpRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.ReSendOtpResponseModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.SendOtpRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.sendOtp.SendOtpResponseModel
import com.hkapps.hygienekleen.features.auth.register.model.verifyOtp.VerifyOtpRequestModel
import com.hkapps.hygienekleen.features.auth.register.model.verifyOtp.VerifyOtpResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class DaftarViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val sendOtpResponseModel = MutableLiveData<SendOtpResponseModel>()
    private val resendOtpResponseModel = MutableLiveData<ReSendOtpResponseModel>()
    private val verifyOtpResponse = MutableLiveData<VerifyOtpResponse>()
    private val passwordResponse = MutableLiveData<PasswordResponse>()
    private val isValidEmail = MutableLiveData<Boolean>()
    private val isEmail = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    var isLoadings: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    @Inject
    lateinit var repository: DaftarRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun sendOtp(email: String, nuc: String) {
        val sendOtpRequestModel = SendOtpRequestModel(email, nuc)
        compositeDisposable.add(
            repository.sendOtp(sendOtpRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SendOtpResponseModel>() {
                    override fun onSuccess(t: SendOtpResponseModel) {
                        sendOtpResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error =
                                gson.fromJson(errorBody?.string(), SendOtpResponseModel::class.java)
                            sendOtpResponseModel.value = error
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun resendOtp(email: String, nuc: String) {
        val resendOtpRequestModel = ReSendOtpRequestModel(email, nuc)
        compositeDisposable.add(
            repository.resendOtp(resendOtpRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReSendOtpResponseModel>() {
                    override fun onSuccess(t: ReSendOtpResponseModel) {
                        resendOtpResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error =
                                gson.fromJson(
                                    errorBody?.string(),
                                    ReSendOtpResponseModel::class.java
                                )
                            resendOtpResponseModel.value = error
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun verifyOtp(email: String, otp: String) {
        val verifyOtpRequestModel = VerifyOtpRequestModel(email, otp)
        compositeDisposable.add(
            repository.verifyOtpRegis(verifyOtpRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VerifyOtpResponse>() {
                    override fun onSuccess(t: VerifyOtpResponse) {
                        verifyOtpResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error =
                                gson.fromJson(errorBody?.string(), VerifyOtpResponse::class.java)
                            verifyOtpResponse.value = error
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putPasswordRegis(email: String, nuc: String, password: String, confirmPass: String) {
        val passwordRequestModel = PasswordRequestModel(email, nuc, password, confirmPass)
        compositeDisposable.add(
            repository.passwordRegis(passwordRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PasswordResponse>() {
                    override fun onSuccess(t: PasswordResponse) {
                        passwordResponse.value = t
                        isLoadings?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                PasswordResponse::class.java
                            )
                            passwordResponse.value = error
                            isLoadings?.value = false
                        } else {
                            isLoadings?.value = true
                        }
                    }

                })
        )
    }

    fun validate(email: String) {
        if (!validateEmail(email)) {
            return
        }
        isValidEmail.value = true
    }

    private fun validateEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isEmail.value = true
            return true
        } else {
            isEmail.value = false
            return false
        }
    }

    fun getSendOtpModel(): MutableLiveData<SendOtpResponseModel> {
        return sendOtpResponseModel
    }

    fun getReSendOtpModel(): MutableLiveData<ReSendOtpResponseModel> {
        return resendOtpResponseModel
    }

    fun getVerifyOtpModel(): MutableLiveData<VerifyOtpResponse> {
        return verifyOtpResponse
    }

    fun getIsValidEmail(): MutableLiveData<Boolean> {
        return isEmail
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}