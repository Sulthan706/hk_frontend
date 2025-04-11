package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository.HomeRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listSlipGaji.ListSlipGajiResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.reportAttendance.ReportAttendanceResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HomeReportViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val reportAttendanceModel = MutableLiveData<ReportAttendanceResponse>()
    val listSlipGajiModel = MutableLiveData<ListSlipGajiResponse>()

    @Inject
    lateinit var repository: HomeRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getReportAttendance(
        employeeId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getReportAttendance(employeeId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReportAttendanceResponse>() {
                    override fun onSuccess(t: ReportAttendanceResponse) {
                        reportAttendanceModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReportAttendanceResponse::class.java
                                )
                                reportAttendanceModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getListSlipGaji(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getListSlipGaji(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListSlipGajiResponse>() {
                    override fun onSuccess(t: ListSlipGajiResponse) {
                        listSlipGajiModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListSlipGajiResponse::class.java
                                )
                                listSlipGajiModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
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