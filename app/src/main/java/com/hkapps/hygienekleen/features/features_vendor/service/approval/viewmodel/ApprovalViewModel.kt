package com.hkapps.hygienekleen.features.features_vendor.service.approval.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.repository.ApprovalRepository
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.approvalAttendance.ApprovalAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.ListAttendanceApprovalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.HistoryUserFlyingResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class ApprovalViewModel(application: Application) : AndroidViewModel(application) {

    val attendanceApprovalResponse = MutableLiveData<ListAttendanceApprovalResponse>()
    val attendanceDeclineResponse = MutableLiveData<ApprovalAttendanceResponse>()
    val attendanceInApproveResponse = MutableLiveData<ApprovalAttendanceResponse>()
    val attendanceOutApproveResponse = MutableLiveData<ApprovalAttendanceResponse>()
    val getHistoryUserFlyingModel = MutableLiveData<HistoryUserFlyingResponse>()

    val attendanceDeclineManagementResponse = MutableLiveData<ApprovalAttendanceResponse>()
    val attendanceInApproveManagementResponse = MutableLiveData<ApprovalAttendanceResponse>()
    val attendanceOutApproveManagementResponse = MutableLiveData<ApprovalAttendanceResponse>()
    val attendanceApprovalManagementResponse = MutableLiveData<ListAttendanceApprovalResponse>()
    val historyUserFlyingManagementResponse = MutableLiveData<HistoryUserFlyingResponse>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDispossable = CompositeDisposable()
    private val isNull = MutableLiveData<Boolean>()
    private val isConnectionTimeout = MutableLiveData<Boolean>()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: ApprovalRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListAttendanceApproval (
        projectCode: String,
        page: Int,
        perPage: Int
    ) {
        compositeDispossable.add(
            repository.getListAttendanceApproval(projectCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAttendanceApprovalResponse>() {
                    override fun onSuccess(t: ListAttendanceApprovalResponse) {
                        attendanceApprovalResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500 -> {
                                    Toast.makeText(
                                        context,
                                        "Internal server error 500.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ListAttendanceApprovalResponse::class.java
                                    )
                                    attendanceApprovalResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun submitDeclineAttendance (
        employeeId: Int,
        idUserFlying: Int
    ) {
        compositeDispossable.add(
            repository.submitDeclineAttendance(employeeId, idUserFlying)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalAttendanceResponse>() {
                    override fun onSuccess(t: ApprovalAttendanceResponse) {
                        attendanceDeclineResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ApprovalAttendanceResponse::class.java
                                    )
                                    attendanceDeclineResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun submitApproveAttendanceIn(
        employeeId: Int,
        idUserFlying: Int
    ) {
        compositeDispossable.add(
            repository.submitApproveAttendanceIn(employeeId, idUserFlying)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalAttendanceResponse>() {
                    override fun onSuccess(t: ApprovalAttendanceResponse) {
                        attendanceInApproveResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ApprovalAttendanceResponse::class.java
                                    )
                                    attendanceInApproveResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun submitApproveAttendanceOut(
        employeeId: Int,
        idUserFlying: Int
    ) {
        compositeDispossable.add(
            repository.submitApproveAttendanceOut(employeeId, idUserFlying)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalAttendanceResponse>() {
                    override fun onSuccess(t: ApprovalAttendanceResponse) {
                        attendanceOutApproveResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ApprovalAttendanceResponse::class.java
                                    )
                                    attendanceOutApproveResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getHistoryUserFlying(
        userId: Int,
        projectCode: String,
        page: Int,
        perPage: Int,
        date: String
    ) {
        compositeDispossable.add(
            repository.getHistoryUserFlying(userId, projectCode, page, perPage, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryUserFlyingResponse>() {
                    override fun onSuccess(t: HistoryUserFlyingResponse) {
                        isLoading?.value = false
                        getHistoryUserFlyingModel.value = t

                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500, 400 -> {
                                    Toast.makeText(
                                        context,
                                        "Gagal mengambil data history",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        HistoryUserFlyingResponse::class.java
                                    )
                                    getHistoryUserFlyingModel.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun submitDeclineAttendanceManagement (
        userId: Int,
        idUserFlying: Int
    ) {
        compositeDispossable.add(
            repository.submitDeclineAttendanceManagement(userId, idUserFlying)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalAttendanceResponse>() {
                    override fun onSuccess(t: ApprovalAttendanceResponse) {
                        isLoading?.value = false
                        attendanceDeclineManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ApprovalAttendanceResponse::class.java
                                    )
                                    attendanceDeclineManagementResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun submitApproveAttendanceInManagement(
        userId: Int,
        idUserFlying: Int
    ) {
        compositeDispossable.add(
            repository.submitApproveAttendanceInManagement(userId, idUserFlying)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalAttendanceResponse>() {
                    override fun onSuccess(t: ApprovalAttendanceResponse) {
                        isLoading?.value = false
                        attendanceInApproveManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ApprovalAttendanceResponse::class.java
                                    )
                                    attendanceInApproveManagementResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })

        )
    }

    fun submitApproveAttendanceOutManagement(
        userId: Int,
        idUserFlying: Int
    ) {
        compositeDispossable.add(
            repository.submitApproveAttendanceOutManagement(userId, idUserFlying)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalAttendanceResponse>() {
                    override fun onSuccess(t: ApprovalAttendanceResponse) {
                        isLoading?.value = false
                        attendanceOutApproveManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ApprovalAttendanceResponse::class.java
                                    )
                                    attendanceOutApproveManagementResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListAttendanceApprovalManagement (
        userId: Int,
        page: Int,
        perPage: Int
    ) {
        compositeDispossable.add(
            repository.getListAttendanceApprovalManagement(userId, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAttendanceApprovalResponse>() {
                    override fun onSuccess(t: ListAttendanceApprovalResponse) {
                        attendanceApprovalManagementResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500 -> {
                                    Toast.makeText(
                                        context,
                                        "Internal server error 500.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        ListAttendanceApprovalResponse::class.java
                                    )
                                    attendanceApprovalManagementResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getHistoryUserFlyingManagement(
        userId: Int,
        page: Int,
        perPage: Int,
        date: String
    ) {
        compositeDispossable.add(
            repository.getHistoryUserFlyingManagement(userId, page, perPage, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryUserFlyingResponse>() {
                    override fun onSuccess(t: HistoryUserFlyingResponse) {
                        historyUserFlyingManagementResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            when (e.response()!!.code()) {
                                403, 401 -> {
                                    Toast.makeText(
                                        context,
                                        "UNAUTHORIZED.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                500, 400 -> {
                                    Toast.makeText(
                                        context,
                                        "Gagal mengambil data history",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val errorBody = e.response()?.errorBody()
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody?.string(),
                                        HistoryUserFlyingResponse::class.java
                                    )
                                    historyUserFlyingManagementResponse.value = error
                                }
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
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