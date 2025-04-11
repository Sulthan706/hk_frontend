package com.hkapps.academy.features.features_participants.training.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.academy.di.DaggerAcademyOperationComponent
import com.hkapps.academy.features.features_participants.training.data.repository.TrainingParticipantRepository
import com.hkapps.academy.features.features_participants.training.model.listTraining.TrainingsCalendarResponse
import com.hkapps.academy.features.features_participants.training.model.scheduleTraining.ScheduleTrainingResponse
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

class TrainingParticipantViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    var scheduleTrainingModel = MutableLiveData<ScheduleTrainingResponse>()
    var listTrainingScheduleModel = MutableLiveData<TrainingsCalendarResponse>()

    @Inject
    lateinit var repository: TrainingParticipantRepository

    init {
        DaggerAcademyOperationComponent.create().injectRepository(this)
    }

    fun getScheduleTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getScheduleTraining(userNuc, projectCode, levelJabatan, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ScheduleTrainingResponse>() {
                    override fun onSuccess(t: ScheduleTrainingResponse) {
                        scheduleTrainingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ScheduleTrainingResponse::class.java
                                )
                                scheduleTrainingModel.value = error
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

    fun getListTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListTraining(userNuc, projectCode, levelJabatan, date, region, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TrainingsCalendarResponse>() {
                    override fun onSuccess(t: TrainingsCalendarResponse) {
                        listTrainingScheduleModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TrainingsCalendarResponse::class.java
                                )
                                listTrainingScheduleModel.value = error
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