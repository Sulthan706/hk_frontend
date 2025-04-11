package com.hkapps.hygienekleen.features.auth.forgotpass.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.auth.forgotpass.data.repository.ForgotPassRepository
import com.hkapps.hygienekleen.features.auth.forgotpass.model.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class ForgotPassViewModel(application: Application) : AndroidViewModel(application) {
    private val forgotModel = MutableLiveData<ForgotModel>()
    private val forgotOTPModel = MutableLiveData<ForgotOTPResponseModel>()
    private val forgotPassChangePassModel = MutableLiveData<ForgotPassChangePassResponseModel>()
    private val isValidEmail = MutableLiveData<Boolean>()
    private val isEmail = MutableLiveData<Boolean>()
    private val isValidEmailPassword = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    var isLoadings: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDispossable = CompositeDisposable()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: ForgotPassRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun postForgotPassVM(email: String) {
        val forgotPassRequestModel = ForgotRequestModel(email)
        compositeDispossable.add(
            repository.forgot(forgotPassRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ForgotModel>() {
                    override fun onSuccess(t: ForgotModel) {
                        forgotModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Forgot", "Error Forgot")
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(errorBody?.string(), ForgotModel::class.java)
                            forgotModel.value = error
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }


    fun getEmailOTPVM(email: String, otp: String) {
        val forgotPassOTPRequestModel = ForgotOTPRequestModel(email,otp)
        compositeDispossable.add(
            repository.forgotOTP(forgotPassOTPRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ForgotOTPResponseModel>() {
                    override fun onSuccess(t: ForgotOTPResponseModel) {
                        forgotOTPModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ForgotOTPResponseModel::class.java
                            )
                            forgotOTPModel.value = error
                            isLoading?.value = false

                            Log.e("OTP", "Error OTP$error")
                        } else {
                            Log.e("OTP", "Error OTP Connection")
                            isLoading?.value = true
                        }
                    }
                })
        )
    }


    fun putChangePassVM(email: String, pass: String, confirm: String) {
        val forgotChangePassModel = ForgotPassChangePassRequestModel(email, pass, confirm)
        compositeDispossable.add(
            repository.forgotPassChangePass(forgotChangePassModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ForgotPassChangePassResponseModel>() {
                    override fun onSuccess(t: ForgotPassChangePassResponseModel) {
                        forgotPassChangePassModel.value = t
                        isLoadings?.value = false
                        Log.d("success", "success Changepass")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ForgotPassChangePassResponseModel::class.java
                            )
                            forgotPassChangePassModel.value = error
                            isLoadings?.value = false

                            Log.e("changepass", "Error Changepass$error")
                        } else {
                            Log.e("changepass", "Error Changepass")
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
        isValidEmailPassword.value = true
    }

    private fun validateEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValidEmail.value = true
            isEmail.value = true
            return true
        } else {
            isValidEmail.value = false
            return false
        }
    }

    fun getForgotModel(): MutableLiveData<ForgotModel> {
        return forgotModel
    }

    fun getForgotOTPModel(): MutableLiveData<ForgotOTPResponseModel> {
        return forgotOTPModel
    }

    fun getIsValidEmail(): MutableLiveData<Boolean> {
        return isValidEmail
    }

    override fun onCleared() {
        super.onCleared()
        compositeDispossable.dispose()
    }

}