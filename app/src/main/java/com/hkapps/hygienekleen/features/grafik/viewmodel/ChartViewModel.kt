package com.hkapps.hygienekleen.features.grafik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent

import com.hkapps.hygienekleen.features.grafik.data.repository.ChartRepository
import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.ListTimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.ListTurnOverResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.TurnOverResponse
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

class ChartViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val chartAbsenceStaffModel = MutableLiveData<ChartAbsenceStaffResponse>()

    val timeSheetModel = MutableLiveData<TimeSheetResponse>()

    val listTimeSheetModel = MutableLiveData<ListTimeSheetResponse>()

    val turnOverModel = MutableLiveData<TurnOverResponse>()

    val listTurnOverModel = MutableLiveData<ListTurnOverResponse>()

    @Inject
    lateinit var chartRepository: ChartRepository

    init{
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getAbsenceStaff(projectCode : String){
        compositeDisposable.add(
            chartRepository.getDataAbsenceStaff(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChartAbsenceStaffResponse>(){
                    override fun onSuccess(t: ChartAbsenceStaffResponse) {
                        chartAbsenceStaffModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ChartAbsenceStaffResponse::class.java
                                )
                                chartAbsenceStaffModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDataTimeSheet(projectCode : String){
        compositeDisposable.add(
            chartRepository.getDataTimeSheet(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TimeSheetResponse>(){
                    override fun onSuccess(t: TimeSheetResponse) {
                        timeSheetModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TimeSheetResponse::class.java
                                )
                                timeSheetModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListTimeSheet(projectCode : String){
        compositeDisposable.add(
            chartRepository.getDataListTimeSheet(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListTimeSheetResponse>(){
                    override fun onSuccess(t: ListTimeSheetResponse) {
                        listTimeSheetModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListTimeSheetResponse::class.java
                                )
                                listTimeSheetModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDataTurnOver(projectCode : String){
        compositeDisposable.add(
            chartRepository.getDataTurnOver(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TurnOverResponse>(){
                    override fun onSuccess(t: TurnOverResponse) {
                        turnOverModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TurnOverResponse::class.java
                                )
                                turnOverModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListTurnOver(projectCode : String){
        compositeDisposable.add(
            chartRepository.getListDataTurnOver(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListTurnOverResponse>(){
                    override fun onSuccess(t: ListTurnOverResponse) {
                        listTurnOverModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListTurnOverResponse::class.java
                                )
                                listTurnOverModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
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