package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.repository.AttendanceTrackingRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.BranchNameManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew.HistoryAttendanceNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.TotalProjectBranchResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listBranch.AllBranchVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProjectNew.ProjectCountDateNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.AttendancesProjectManagementResponse
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

class AttendanceTrackingViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val historyAllAttendanceModel = MutableLiveData<AttendancesProjectManagementResponse>()
    val historyAttendanceDateModel = MutableLiveData<AttendancesProjectManagementResponse>()
    val countProjectDateModel = MutableLiveData<ProjectCountDateNewResponse>()
    val branchManagementModel = MutableLiveData<BranchNameManagementResponse>()
    val allBranchModel = MutableLiveData<AllBranchVisitResponse>()
    val totalProjectBranchModel = MutableLiveData<TotalProjectBranchResponse>()
    val historyAttendanceNewModel = MutableLiveData<HistoryAttendanceNewResponse>()


    @Inject
    lateinit var repository: AttendanceTrackingRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getHistoryAllAttendance (userId: Int) {
        compositeDisposable.add(
            repository.getAllAttendanceHistory(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendancesProjectManagementResponse>() {
                    override fun onSuccess(t: AttendancesProjectManagementResponse) {
                        if (t.code == 200) {
                            historyAllAttendanceModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendancesProjectManagementResponse::class.java
                                )
                                historyAllAttendanceModel.value = error
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

    fun getAttendanceHistoryDate(
        userId: Int,
        startDate: String
    ) {
        compositeDisposable.add(
            repository.getAttendanceHistoryDate(userId, startDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendancesProjectManagementResponse>() {
                    override fun onSuccess(t: AttendancesProjectManagementResponse) {
                        if (t.code == 200) {
                            historyAttendanceDateModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendancesProjectManagementResponse::class.java
                                )
                                historyAttendanceDateModel.value = error
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

    fun getCountProjectDate(
        userId: Int,
        startDate: String,
        endDate: String
    ) {
        compositeDisposable.add(
            repository.getCountProjectDate(userId, startDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectCountDateNewResponse>() {
                    override fun onSuccess(t: ProjectCountDateNewResponse) {
                        if (t.code == 200) {
                            countProjectDateModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectCountDateNewResponse::class.java
                                )
                                countProjectDateModel.value = error
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

    fun getBranchManagement(userId: Int) {
        compositeDisposable.add(
            repository.getBranchManagement(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BranchNameManagementResponse>() {
                    override fun onSuccess(t: BranchNameManagementResponse) {
                        if (t.code == 200) {
                            branchManagementModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BranchNameManagementResponse::class.java
                                )
                                branchManagementModel.value = error
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

    fun getAllBranch() {
        compositeDisposable.add(
            repository.getAllBranch()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AllBranchVisitResponse>() {
                    override fun onSuccess(t: AllBranchVisitResponse) {
                        if (t.code == 200) {
                            allBranchModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AllBranchVisitResponse::class.java
                                )
                                allBranchModel.value = error
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

    fun getTotalProjectBranch(branchCode: String) {
        compositeDisposable.add(
            repository.getTotalProjectBranch(branchCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TotalProjectBranchResponse>() {
                    override fun onSuccess(t: TotalProjectBranchResponse) {
                        if (t.code == 200) {
                            totalProjectBranchModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TotalProjectBranchResponse::class.java
                                )
                                totalProjectBranchModel.value = error
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

    fun getHistoryAttendanceNew(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getHistoryAttendanceNew(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryAttendanceNewResponse>() {
                    override fun onSuccess(t: HistoryAttendanceNewResponse) {
                        historyAttendanceNewModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    HistoryAttendanceNewResponse::class.java
                                )
                                historyAttendanceNewModel.value = error
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
        compositeDisposable.dispose()
    }
}