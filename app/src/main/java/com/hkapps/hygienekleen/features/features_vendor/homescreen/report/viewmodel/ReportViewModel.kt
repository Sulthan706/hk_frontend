package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.repository.ReportRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.dailyAttendanceReport.DailyAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listEventCalendar.ListCalendarReportResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.ListLateReportResponse
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

class ReportViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val dailyAttendanceReportResponse = MutableLiveData<DailyAttendanceReportResponse>()
    val listLateReportResponse = MutableLiveData<ListLateReportResponse>()
    val listCalendarReportResponse = MutableLiveData<ListCalendarReportResponse>()

    @Inject
    lateinit var repository: ReportRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getDailyAttendanceReport(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getDailyAttendanceReport(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyAttendanceReportResponse>() {
                    override fun onSuccess(t: DailyAttendanceReportResponse) {
                        dailyAttendanceReportResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyAttendanceReportResponse::class.java
                                )
                                dailyAttendanceReportResponse.value = error
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

    fun getListLateReport(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getListLateReport(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListLateReportResponse>() {
                    override fun onSuccess(t: ListLateReportResponse) {
                        listLateReportResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListLateReportResponse::class.java
                                )
                                listLateReportResponse.value = error
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

    fun getListCalendarReport(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getListCalendarReport(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCalendarReportResponse>() {
                    override fun onSuccess(t: ListCalendarReportResponse) {
                        listCalendarReportResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCalendarReportResponse::class.java
                                )
                                listCalendarReportResponse.value = error
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