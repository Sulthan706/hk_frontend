package com.hkapps.academy.features.authentication.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.academy.di.DaggerAcademyOperationComponent
import com.hkapps.academy.features.authentication.data.repository.AuthAcademyRepository
import com.hkapps.academy.features.authentication.model.login.LoginAcademyResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthAcademyViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val loginAcademyModel = MutableLiveData<LoginAcademyResponse>()

    @Inject
    lateinit var repository: AuthAcademyRepository

    init {
        DaggerAcademyOperationComponent.create().injectRepository(this)
    }

    fun loginAcademy(
        userNuc: String
    ) {
        compositeDisposable.add(
            repository.loginAcademy(userNuc)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginAcademyResponse>() {
                    override fun onSuccess(t: LoginAcademyResponse) {
                        loginAcademyModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        Log.e("AuthAcademy", "onError: error login")
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (e.response()!!.code() == 403) {
                                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                                }
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LoginAcademyResponse::class.java
                                )
                                loginAcademyModel.value = error
                                Log.d("AuthAcademy", "onError: $error")
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}