package com.hkapps.hygienekleen.features.features_client.dashboardProject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.repository.DashboardProjectRepository
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.DetailDashboardProjectResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.historyAttendance.AttendanceProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.EmployeeProjectTeamResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listShift.ShiftProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listSubLoc.SubLocProjectClientResponse
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

class DashboardProjectViewModel (application: Application) : AndroidViewModel(application) {

    private val compositeDispossable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val detailDashboardProjectModel = MutableLiveData<DetailDashboardProjectResponse>()
    val subLocProjectModel = MutableLiveData<SubLocProjectClientResponse>()
    val shiftProjectModel = MutableLiveData<ShiftProjectClientResponse>()
    val teamEmployeeProjectModel = MutableLiveData<EmployeeProjectTeamResponse>()
    val historyAttendanceProjectModel = MutableLiveData<AttendanceProjectClientResponse>()

    @Inject
    lateinit var repository: DashboardProjectRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getDetailDashboardProject(projectCode: String) {
        compositeDispossable.add(
            repository.getDetailDashboardProject(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailDashboardProjectResponse>() {
                    override fun onSuccess(t: DetailDashboardProjectResponse) {
                        detailDashboardProjectModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailDashboardProjectResponse::class.java
                                )
                                detailDashboardProjectModel.value = error
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

    fun getSubLocProjectClient(
        projectCode: String,
        locationId: Int
    ) {
        compositeDispossable.add(
            repository.getSubLocProjectClient(projectCode, locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubLocProjectClientResponse>() {
                    override fun onSuccess(t: SubLocProjectClientResponse) {
                        subLocProjectModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubLocProjectClientResponse::class.java
                                )
                                subLocProjectModel.value = error
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

    fun getShiftProjectClient(
        projectCode: String,
        locationId: Int,
        subLocationId: Int
    ) {
        compositeDispossable.add(
            repository.getShiftProjectClient(projectCode, locationId, subLocationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ShiftProjectClientResponse>() {
                    override fun onSuccess(t: ShiftProjectClientResponse) {
                        shiftProjectModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ShiftProjectClientResponse::class.java
                                )
                                shiftProjectModel.value = error
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

    fun getListTeamEmployeeClient(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ) {
        compositeDispossable.add(
            repository.getListTeamEmployeeClient(projectCode, date, shiftId, locationId, subLocationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EmployeeProjectTeamResponse>() {
                    override fun onSuccess(t: EmployeeProjectTeamResponse) {
                        teamEmployeeProjectModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EmployeeProjectTeamResponse::class.java
                                )
                                teamEmployeeProjectModel.value = error
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

    fun getHistoryAttendanceProject(
        projectCode: String,
        date: String
    ) {
        compositeDispossable.add(
            repository.getHistoryAttendanceProject(projectCode, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceProjectClientResponse>() {
                    override fun onSuccess(t: AttendanceProjectClientResponse) {
                        historyAttendanceProjectModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceProjectClientResponse::class.java
                                )
                                historyAttendanceProjectModel.value = error
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
        compositeDispossable.dispose()
    }
}