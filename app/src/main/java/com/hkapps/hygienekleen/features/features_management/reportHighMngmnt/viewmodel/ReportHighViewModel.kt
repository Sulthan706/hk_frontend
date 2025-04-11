package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.repository.ReportHighRepository
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.AttendanceReportHighResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.countAttendanceReport.CountAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listBranch.BranchesAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listProject.ProjectsAttendanceReportResponse
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

class ReportHighViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val attendanceReportHighResponse = MutableLiveData<AttendanceReportHighResponse>()
    val branchesAttendanceReportResponse = MutableLiveData<BranchesAttendanceReportResponse>()
    val countAttendanceReportResponse = MutableLiveData<CountAttendanceReportResponse>()
    val projectAttendanceReportResponse = MutableLiveData<ProjectsAttendanceReportResponse>()

    @Inject
    lateinit var repository: ReportHighRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String,
        order: String,
        whatToOrder: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListAttendanceReportHigh(
                branchCode, projectCode, startAt, endAt, order, whatToOrder, page, size
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceReportHighResponse>() {
                    override fun onSuccess(t: AttendanceReportHighResponse) {
                        attendanceReportHighResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceReportHighResponse::class.java
                                )
                                attendanceReportHighResponse.value = error
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

    fun getListBranchAttendanceReport() {
        compositeDisposable.add(
            repository.getListBranchAttendanceReport()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BranchesAttendanceReportResponse>() {
                    override fun onSuccess(t: BranchesAttendanceReportResponse) {
                        branchesAttendanceReportResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BranchesAttendanceReportResponse::class.java
                                )
                                branchesAttendanceReportResponse.value = error
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

    fun getCountAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String,
    ) {
        compositeDisposable.add(
            repository.getCountAttendanceReportHigh(branchCode, projectCode, startAt, endAt)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountAttendanceReportResponse>() {
                    override fun onSuccess(t: CountAttendanceReportResponse) {
                        countAttendanceReportResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CountAttendanceReportResponse::class.java
                                )
                                countAttendanceReportResponse.value = error
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

    fun getListProjectAttendanceReport(
        branchCode: String
    ) {
        compositeDisposable.add(
            repository.getListProjectAttendanceReport(branchCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAttendanceReportResponse>() {
                    override fun onSuccess(t: ProjectsAttendanceReportResponse) {
                        projectAttendanceReportResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAttendanceReportResponse::class.java
                                )
                                projectAttendanceReportResponse.value = error
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