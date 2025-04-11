package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.repository.ScheduleRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel.MidScheduleResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class ScheduleViewModel: ViewModel() {
    val scheduleResponseModel = MutableLiveData<ScheduleResponseModel>()
    val midScheduleResponseModel = MutableLiveData<MidScheduleResponseModel>()
    private val compositeDisposable = CompositeDisposable()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    @Inject
    lateinit var repository: ScheduleRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getScheduleVM(employeeId: Int, date: String, page: Int) {
        compositeDisposable.add(
            repository.getSchedule(employeeId, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ScheduleResponseModel>() {
                    override fun onSuccess(t: ScheduleResponseModel) {
                        scheduleResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                scheduleResponseModel::class.java
                            )
//                            scheduleResponseModel.value = error
                        }
                    }
                })
        )
    }


    fun getSchResponseModel(): MutableLiveData<ScheduleResponseModel> {
        return scheduleResponseModel
    }


    fun getMidScheduleVM(employeeId: Int, date: String, page: Int) {
        compositeDisposable.add(
            repository.getMidSchedule(employeeId, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MidScheduleResponseModel>() {
                    override fun onSuccess(t: MidScheduleResponseModel) {
                        midScheduleResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                midScheduleResponseModel::class.java
                            )
//                            scheduleResponseModel.value = error
                        }
                    }
                })
        )
    }


    fun getMidSchResponseModel(): MutableLiveData<MidScheduleResponseModel> {
        return midScheduleResponseModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}