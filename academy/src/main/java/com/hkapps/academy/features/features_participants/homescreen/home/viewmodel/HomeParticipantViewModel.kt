package com.hkapps.academy.features.features_participants.homescreen.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.academy.di.DaggerAcademyOperationComponent
import com.hkapps.academy.features.features_participants.homescreen.home.data.repository.HomeParticipantRepository
import com.hkapps.academy.features.features_participants.homescreen.home.model.listClass.ClassesHomeResponse
import com.hkapps.academy.features.features_participants.homescreen.home.model.listTraining.TrainingsHomeResponse
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

class HomeParticipantViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    var listClassHomeModel = MutableLiveData<ClassesHomeResponse>()
    var listTrainingHomeModel = MutableLiveData<TrainingsHomeResponse>()

    @Inject
    lateinit var repository: HomeParticipantRepository

    init {
        DaggerAcademyOperationComponent.create().injectRepository(this)
    }

    fun getListClassHome(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListClassHome(userNuc, projectCode, levelJabatan, date, region, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClassesHomeResponse>() {
                    override fun onSuccess(t: ClassesHomeResponse) {
                        listClassHomeModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClassesHomeResponse::class.java
                                )
                                listClassHomeModel.value = error
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

    fun getListTrainingHome(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListTrainingHome(userNuc, projectCode, levelJabatan, date, region, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TrainingsHomeResponse>() {
                    override fun onSuccess(t: TrainingsHomeResponse) {
                        listTrainingHomeModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TrainingsHomeResponse::class.java
                                )
                                listTrainingHomeModel.value = error
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