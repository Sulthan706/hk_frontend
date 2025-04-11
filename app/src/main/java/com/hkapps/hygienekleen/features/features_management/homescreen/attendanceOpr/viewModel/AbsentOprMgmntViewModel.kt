package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.repository.AbsentOprMgmntRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DacOperationalAbsentMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.DetailAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.DetailAbsentByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listBranch.ListBranchAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.ListCountAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.ListProjectByBranchAbsentOprResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.ListProjectByUserAbsentOprResponse
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

class AbsentOprMgmntViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listBranchAbsentOprResponseModel = MutableLiveData<ListBranchAbsentOprMgmntResponse>()
    val listProjectByBranchAbsentOprResponseModel = MutableLiveData<ListProjectByBranchAbsentOprResponse>()
    val listProjectByUserAbsentOprResponseModel = MutableLiveData<ListProjectByUserAbsentOprResponse>()
    val listCountAbsentOprResponseModel = MutableLiveData<ListCountAbsentOprMgmntResponse>()
    val detailAbsentOprResponseModel = MutableLiveData<DetailAbsentOprMgmntResponse>()
    val searchAbsentOprResponseModel = MutableLiveData<ListProjectByBranchAbsentOprResponse>()
    val detailAbsentByStatusResponseModel = MutableLiveData<DetailAbsentByStatusResponseModel>()
    val dacOperationalResponseModel = MutableLiveData<DacOperationalAbsentMgmntResponse>()

    @Inject
    lateinit var repository: AbsentOprMgmntRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListBranchAbsentOpr() {
        compositeDisposable.add(
            repository.getListBranch()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListBranchAbsentOprMgmntResponse>() {
                    override fun onSuccess(t: ListBranchAbsentOprMgmntResponse) {
                        if (t.code == 200) {
                            listBranchAbsentOprResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListBranchAbsentOprMgmntResponse::class.java
                                )
                                listBranchAbsentOprResponseModel.value = error
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

    fun getListProjectByBranch(branchCode: String, page: Int, perPage: Int) {
        compositeDisposable.add(
            repository.getListProjectByBranch(branchCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectByBranchAbsentOprResponse>() {
                    override fun onSuccess(t: ListProjectByBranchAbsentOprResponse) {
                        if (t.code == 200) {
                            listProjectByBranchAbsentOprResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectByBranchAbsentOprResponse::class.java
                                )
                                listProjectByBranchAbsentOprResponseModel.value = error
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

    fun getListProjectByUser(employeeId: Int) {
        compositeDisposable.add(
            repository.getListProjectByUser(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectByUserAbsentOprResponse>() {
                    override fun onSuccess(t: ListProjectByUserAbsentOprResponse) {
                        if (t.code == 200) {
                            listProjectByUserAbsentOprResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectByUserAbsentOprResponse::class.java
                                )
                                listProjectByUserAbsentOprResponseModel.value = error
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

    fun getListCountAbsentOpr(projectCode: String, month: Int, year: Int, jobRole: String) {
        compositeDisposable.add(
            repository.getListAbsentCountOpr(projectCode, month, year, jobRole)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCountAbsentOprMgmntResponse>() {
                    override fun onSuccess(t: ListCountAbsentOprMgmntResponse) {
                        if (t.code == 200) {
                            listCountAbsentOprResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCountAbsentOprMgmntResponse::class.java
                                )
                                listCountAbsentOprResponseModel.value = error
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

    fun getDetailAbsentOpr(projectCode: String, employeeId: Int, month: Int, year: Int) {
        compositeDisposable.add(
            repository.getDetailAbsentOpr(projectCode, employeeId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAbsentOprMgmntResponse>() {
                    override fun onSuccess(t: DetailAbsentOprMgmntResponse) {
                        if (t.code == 200) {
                            detailAbsentOprResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailAbsentOprMgmntResponse::class.java
                                )
                                detailAbsentOprResponseModel.value = error
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

    fun searchProjectByBranch(page: Int, branchCode: String, keywords: String, perPage: Int) {
        compositeDisposable.add(
            repository.searchProject(page, branchCode, keywords, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectByBranchAbsentOprResponse>() {
                    override fun onSuccess(t: ListProjectByBranchAbsentOprResponse) {
                        if (t.code == 200) {
                            searchAbsentOprResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectByBranchAbsentOprResponse::class.java
                                )
                                searchAbsentOprResponseModel.value = error
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

    fun getDetailAbsentByStatusOpr(employeeId: Int,
                                   projectCode: String,
                                   month: Int,
                                   year: Int,
                                   scheduleType: String,
                                   statusAttendance: String){
        compositeDisposable.add(
            repository.getDetailAbsentByStatusOpr(employeeId, projectCode, month, year, scheduleType, statusAttendance)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAbsentByStatusResponseModel>(){

                    override fun onSuccess(t: DetailAbsentByStatusResponseModel) {
                        if (t.code == 200){
                            detailAbsentByStatusResponseModel.value = t
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
                                    DetailAbsentByStatusResponseModel::class.java
                                )
                                detailAbsentByStatusResponseModel.value = error
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
    fun getDacOperational(employeeId: Int, projectId: String, idDetailEmployeeProject: Int) {
        compositeDisposable.add(
            repository.getDacOperational(employeeId, projectId, idDetailEmployeeProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DacOperationalAbsentMgmntResponse>() {
                    override fun onSuccess(t: DacOperationalAbsentMgmntResponse) {
                        if (t.code == 200) {
                            dacOperationalResponseModel.value = t
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
                                    DacOperationalAbsentMgmntResponse::class.java
                                )
                                dacOperationalResponseModel.value = error
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