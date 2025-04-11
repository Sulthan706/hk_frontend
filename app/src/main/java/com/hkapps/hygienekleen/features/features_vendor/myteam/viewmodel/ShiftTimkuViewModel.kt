package com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.myteam.data.repository.TimkuRepository
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsent.CountAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsentMidLevel.CountAbsentMidLevelResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.OperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel.ShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.TeamleadResponseModel
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

class ShiftTimkuViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val getListShiftModel = MutableLiveData<ShiftResponseModel>()
    private val getListOperatorModel = MutableLiveData<OperatorResponseModel>()
    private val getListLeaderModel = MutableLiveData<TeamleadResponseModel>()
    private val getListSpvModel = MutableLiveData<SupervisorResponseModel>()
    private val getCountAbsentOprModel = MutableLiveData<CountAbsentResponseModel>()
    private val getCountAbsentLeaderModel = MutableLiveData<CountAbsentMidLevelResponseModel>()
    private val getCountAbsentSpvModel = MutableLiveData<CountAbsentMidLevelResponseModel>()
    val listLeaderNewModel = MutableLiveData<TeamleadResponseModel>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    @Inject
    lateinit var repository: TimkuRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getCountAbsentSpv(projectCode: String, employeeId: Int) {
        compositeDisposable.add(
            repository.getCountAbsentSpv(projectCode, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountAbsentMidLevelResponseModel>() {
                    override fun onSuccess(t: CountAbsentMidLevelResponseModel) {
                        if (t.code == 200) {
                            getCountAbsentSpvModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CountAbsentMidLevelResponseModel::class.java
                                )
                                getCountAbsentSpvModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getCountAbsentLeader(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getCountAbsentLeader(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountAbsentMidLevelResponseModel>() {
                    override fun onSuccess(t: CountAbsentMidLevelResponseModel) {
                        if (t.code == 200) {
                            getCountAbsentLeaderModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CountAbsentMidLevelResponseModel::class.java
                                )
                                getCountAbsentLeaderModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getCountAbsentOperator(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getCountAbsentOperator(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountAbsentResponseModel>() {
                    override fun onSuccess(t: CountAbsentResponseModel) {
                        if (t.code == 200) {
                            getCountAbsentOprModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CountAbsentResponseModel::class.java
                                )
                                getCountAbsentOprModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getListSupervisor(projectCode: String) {
        compositeDisposable.add(
            repository.getListSpvTimku(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SupervisorResponseModel>() {
                    override fun onSuccess(t: SupervisorResponseModel) {
                        if (t.code == 200) {
                            getListSpvModel.value = t
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
                                    SupervisorResponseModel::class.java
                                )
                                getListSpvModel.value = error
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

    fun getListLeader(employeeId: Int, projectCode: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getListLeader(employeeId, projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TeamleadResponseModel>() {
                    override fun onSuccess(t: TeamleadResponseModel) {
                        if (t.code == 200) {
                            getListLeaderModel.value = t
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
                                    TeamleadResponseModel::class.java
                                )
                                getListLeaderModel.value = error
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

    fun getListOperator(userId: Int, projectCode: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getListOperator(userId, projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorResponseModel>() {
                    override fun onSuccess(t: OperatorResponseModel) {
                        if (t.code == 200) {
                            getListOperatorModel.value = t
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
                                    OperatorResponseModel::class.java
                                )
                                getListOperatorModel.value = error
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

    fun getListOperatorByDate(userId: Int, projectCode: String, shiftId: Int, date: String) {
        compositeDisposable.add(
            repository.getListOperatorByDate(userId, projectCode, shiftId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorResponseModel>() {
                    override fun onSuccess(t: OperatorResponseModel) {
                        if (t.code == 200) {
                            getListOperatorModel.value = t
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
                                    OperatorResponseModel::class.java
                                )
                                getListOperatorModel.value = error
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

    fun getListShiftTimku(projectCode: String) {
        compositeDisposable.add(
            repository.getListShiftTimku(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ShiftResponseModel>() {
                    override fun onSuccess(t: ShiftResponseModel) {
                        if (t.code == 200) {
                            getListShiftModel.value = t
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
                                    ShiftResponseModel::class.java
                                )
                                getListShiftModel.value = error
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

    fun getListLeaderNew(projectCode: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getListLeaderNew(projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TeamleadResponseModel>() {
                    override fun onSuccess(t: TeamleadResponseModel) {
                        if (t.code == 200) {
                            listLeaderNewModel.value = t
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
                                    TeamleadResponseModel::class.java
                                )
                                listLeaderNewModel.value = error
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

    fun getListShiftResponseModel(): MutableLiveData<ShiftResponseModel> {
        return getListShiftModel
    }

    fun getListOperatorResponseModel(): MutableLiveData<OperatorResponseModel> {
        return getListOperatorModel
    }

    fun getListLeaderResponseModel(): MutableLiveData<TeamleadResponseModel> {
        return getListLeaderModel
    }

    fun getListSpvResponseModel(): MutableLiveData<SupervisorResponseModel> {
        return getListSpvModel
    }

    fun getCountAbsentOprResponseModel(): MutableLiveData<CountAbsentResponseModel> {
        return getCountAbsentOprModel
    }

    fun getCountAbsentLeaderResponseModel(): MutableLiveData<CountAbsentMidLevelResponseModel> {
        return getCountAbsentLeaderModel
    }

    fun getCountAbsentSpvResponseModel(): MutableLiveData<CountAbsentMidLevelResponseModel> {
        return getCountAbsentSpvModel
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}