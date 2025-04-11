package com.hkapps.academy.features.features_trainer.homescreen.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.academy.di.DaggerAcademyOperationComponent
import com.hkapps.academy.features.features_trainer.homescreen.home.data.repository.HomeTrainerRepository
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.ClassesHomeTrainerResponse
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

class HomeTrainerViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    var listClassHomeTrainerModel = MutableLiveData<ClassesHomeTrainerResponse>()
    var listAllClassModel = MutableLiveData<ClassesHomeTrainerResponse>()

    @Inject
    lateinit var repository: HomeTrainerRepository

    init {
        DaggerAcademyOperationComponent.create().injectRepository(this)
    }

    fun getListClassHomeTrainer(
        userNuc: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListClassHomeTrainer(userNuc, date, region, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClassesHomeTrainerResponse>() {
                    override fun onSuccess(t: ClassesHomeTrainerResponse) {
                        listClassHomeTrainerModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClassesHomeTrainerResponse::class.java
                                )
                                listClassHomeTrainerModel.value = error
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

    fun getAllClassTrainer(
        userNuc: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListClassHomeTrainer(userNuc, date, region, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClassesHomeTrainerResponse>() {
                    override fun onSuccess(t: ClassesHomeTrainerResponse) {
                        listAllClassModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClassesHomeTrainerResponse::class.java
                                )
                                listAllClassModel.value = error
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