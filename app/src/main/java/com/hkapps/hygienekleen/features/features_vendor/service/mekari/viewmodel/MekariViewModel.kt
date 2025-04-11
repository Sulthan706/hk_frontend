package com.hkapps.hygienekleen.features.features_vendor.service.mekari.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.repository.MekariRepository
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.SubmitRegisMekariResponse
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.generatetokenmekari.TokenMekariResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.trialmekari.TrialMekariResponseModel
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

class MekariViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val submitRegisMekariResponse = MutableLiveData<SubmitRegisMekariResponse>()
    val checkMekariResponse = MutableLiveData<SubmitRegisMekariResponse>()
    val getTokenMekariResponse = MutableLiveData<TokenMekariResponseModel>()
    val getTrialMekariResponse = MutableLiveData<TrialMekariResponseModel>()

    @Inject
    lateinit var repository: MekariRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun submitRegisMekari (
        employeeId: Int
    ) {
        compositeDisposable.add(
            repository.submitRegisMekari(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitRegisMekariResponse>() {
                    override fun onSuccess(t: SubmitRegisMekariResponse) {
                        submitRegisMekariResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitRegisMekariResponse::class.java
                                )
                                submitRegisMekariResponse.value = error
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

    fun getCheckMekari(
        employeeId: Int
    ) {
        compositeDisposable.add(
            repository.getCheckMekari(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitRegisMekariResponse>() {
                    override fun onSuccess(t: SubmitRegisMekariResponse) {
                        checkMekariResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitRegisMekariResponse::class.java
                                )
                                checkMekariResponse.value = error
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
    fun getTokenMekari(employeeId: Int){
        compositeDisposable.add(
            repository.generateTokenMekari(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TokenMekariResponseModel>(){
                    override fun onSuccess(t: TokenMekariResponseModel) {
                        if (t.code == 200){
                            Log.d("MEKARIT","$t")
                            getTokenMekariResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("MEKARI","$e")
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TokenMekariResponseModel::class.java
                                )
                                getTokenMekariResponse.value = error
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

    fun getTrialMekari(projectCode: String, employeeId: Int){
        compositeDisposable.add(
            repository.getTrialMekari(projectCode, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TrialMekariResponseModel>(){
                    override fun onSuccess(t: TrialMekariResponseModel) {
                        if (t.code == 200){
                            getTrialMekariResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TrialMekariResponseModel::class.java
                                )
                                getTrialMekariResponse.value = error
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

    fun getTrialMekariViewModel(): MutableLiveData<TrialMekariResponseModel>{
        return getTrialMekariResponse
    }

    fun getTokenMekariViewModel(): MutableLiveData<TokenMekariResponseModel>{
        return getTokenMekariResponse
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}