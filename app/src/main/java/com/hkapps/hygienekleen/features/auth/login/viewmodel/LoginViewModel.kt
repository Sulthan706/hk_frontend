package com.hkapps.hygienekleen.features.auth.login.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.auth.login.data.repository.LoginRepository
import com.hkapps.hygienekleen.features.auth.login.model.LoginClientModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginManagementModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginModel
import com.hkapps.hygienekleen.features.auth.login.model.LoginRequestModel
import com.hkapps.hygienekleen.features.auth.login.model.validateimei.ValidateImeiResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class LoginViewModel (application: Application) : AndroidViewModel(application){
    private val loginModel = MutableLiveData<LoginModel>()
    private val loginClientModel = MutableLiveData<LoginClientModel>()
    private val loginManagementModel = MutableLiveData<LoginManagementModel>()
    private val isValidEmail = MutableLiveData<Boolean>()
    private val isEmail = MutableLiveData<Boolean>()
    private val isPhone = MutableLiveData<Boolean>()
    private val isValidPasswordLength = MutableLiveData<Boolean>()
    private val isValidPhoneLength = MutableLiveData<Boolean>()
    private val isValidEmailPassword = MutableLiveData<Boolean>()
    private val compositeDispossable = CompositeDisposable()
    private val validateImeiResponse = MutableLiveData<ValidateImeiResponseModel>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: LoginRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun login(email: String, password: String) {
        val loginRequestModel = LoginRequestModel(email, password)
        compositeDispossable.add(
            repository.login(loginRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginModel>() {
                    override fun onSuccess(t: LoginModel) {
                        loginModel.value = t
                        isLoading?.value = false
                        Log.d("LoginViewModel", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Login", "Error Login")
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(errorBody?.string(), LoginModel::class.java)
                            loginModel.value = error
                            Log.d("LoginViewModel", "onError: False ")
                            isLoading?.value = false
                        } else {
                            Log.d("LoginViewModel", "onError: True ")
                            isLoading?.value = true
//                            Toast.makeText(context, "Terjadi kesalahan, harap periksa koneksi anda.",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        )
    }

    fun loginClient(email: String, password: String) {
        val loginRequestModel = LoginRequestModel(email, password)
        compositeDispossable.add(
            repository.loginClient(loginRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginClientModel>() {
                    override fun onSuccess(t: LoginClientModel) {
                        loginClientModel.value = t
                        isLoading?.value = false
                        Log.d("LoginViewModel", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Login", "Error Login")
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(errorBody?.string(), LoginClientModel::class.java)
                            loginClientModel.value = error
                            Log.d("LoginViewModel", "onError: False ")
                            isLoading?.value = false
                        } else {
                            Log.d("LoginViewModel", "onError: True ")
                            isLoading?.value = false
//                            Toast.makeText(context, "Terjadi kesalahan, harap periksa koneksi anda.",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        )
    }

    fun loginManagement(email: String, password: String) {
        val loginRequestModel = LoginRequestModel(email, password)
        compositeDispossable.add(
            repository.loginManagement(loginRequestModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginManagementModel>() {
                    override fun onSuccess(t: LoginManagementModel) {
                        loginManagementModel.value = t
                        isLoading?.value = false
                        Log.d("LoginViewModel", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Login", "Error Login")
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(errorBody?.string(), LoginManagementModel::class.java)
                            loginManagementModel.value = error
                            Log.d("LoginViewModel", "onError: False ")
                            isLoading?.value = false
                        } else {
                            Log.d("LoginViewModel", "onError: True ")
                            isLoading?.value = false
//                            Toast.makeText(context, "Terjadi kesalahan, harap periksa koneksi anda.",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        )
    }

    fun validateImei(employeeId: String, phoneId: String, phoneModel: String){
        compositeDispossable.add(
            repository.validateImei(employeeId, phoneId, phoneModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ValidateImeiResponseModel>(){
                    override fun onSuccess(t: ValidateImeiResponseModel) {
                        if (t.code == 200){
                            validateImeiResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(errorBody?.string(), ValidateImeiResponseModel::class.java)
                            validateImeiResponse.value = error
                            Log.d("LoginViewModel", "onError: False ")
                            isLoading?.value = false
                        } else {
                            Log.d("LoginViewModel", "onError: True ")
                            isLoading?.value = false
//                            Toast.makeText(context, "Terjadi kesalahan, harap periksa koneksi anda.",Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }


    fun validate(email: String, password: String) {
        if (!validateEmailPhone(email)) {
            return
        }
        if (!validatePasswordLength(password)) {
            return
        }
        isValidEmailPassword.value = true
    }

    fun validateEmailPhone(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValidEmail.value = true
            isEmail.value = true
            isPhone.value = false
            return true
        } else if (Patterns.PHONE.matcher(email).matches()) {
            validatePhoneLength(email)
            isPhone.value = true
            isEmail.value = false
            return isValidPhoneLength.value!!
        } else {
            isValidEmail.value = false
            return false
        }
    }

    private fun validatePhoneLength(email: String) {
        isValidPhoneLength.value = email.length >= 10
    }

    private fun validatePasswordLength(password: String): Boolean {
        isValidPasswordLength.value = password.length >= 0
        return password.length >= 0
    }

    fun validateImeiViewModel(): MutableLiveData<ValidateImeiResponseModel>{
        return validateImeiResponse
    }

    fun getLoginModel(): MutableLiveData<LoginModel> {
        return loginModel
    }

    fun getLoginClientModel(): MutableLiveData<LoginClientModel> {
        return loginClientModel
    }

    fun getLoginManagementModel(): MutableLiveData<LoginManagementModel> {
        return loginManagementModel
    }


    fun getIsValidEmail(): MutableLiveData<Boolean> {
        return isValidEmail
    }

    fun getIsValidPasswordLength(): MutableLiveData<Boolean> {
        return isValidPasswordLength
    }

    fun getIsValidEmailPassword(): MutableLiveData<Boolean> {
        return isValidEmailPassword
    }

    fun getIsValidPhoneLength(): MutableLiveData<Boolean> {
        return isValidPhoneLength
    }

    fun getIsEmail(): MutableLiveData<Boolean> {
        return isEmail
    }

    fun getIsPhone(): MutableLiveData<Boolean> {
        return isPhone
    }

    override fun onCleared() {
        super.onCleared()
        compositeDispossable.dispose()
    }
}