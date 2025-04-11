package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.repository.PeriodicManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicapprovejobmanagement.PeriodApproveJobMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicbystatusmanagement.PeriodicByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarevent.PeriodicCalendarEventResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarmanagement.PeriodicCalendarManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccreatebamanagement.PeriodCreateBaManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicdetailmanagement.PeriodicDetailResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicescallationmanagement.PeriodicEscallationResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodichomemanagement.PeriodicHomeManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicmonitormanagement.PeriodicMonitorManagementResponseModel
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

class PeriodicManagementViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val getHomePeriodicManagementModel = MutableLiveData<PeriodicHomeManagementResponseModel>()
    val getMonitorPeriodicManagementModel = MutableLiveData<PeriodicMonitorManagementResponseModel>()
    val getCalendarPeriodicManagementModel = MutableLiveData<PeriodicCalendarManagementResponseModel>()
    val getCalendarEventPeriodicModel = MutableLiveData<PeriodicCalendarEventResponseModel>()
    val getDetailPeriodicManagementModel = MutableLiveData<PeriodicDetailResponseModel>()
    val getByStatusPeriodicManagementModel = MutableLiveData<PeriodicByStatusResponseModel>()
    val putApproveJobsManagementModel = MutableLiveData<PeriodApproveJobMgmntResponseModel>()
    val putCreateBaManagementModel = MutableLiveData<PeriodCreateBaManagementResponseModel>()
    val getEscallationManagementModel = MutableLiveData<PeriodicEscallationResponseModel>()

    @Inject
    lateinit var repository: PeriodicManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getHomePeriodicManagement(projectCode: String){
        compositeDisposable.add(
            repository.getHomeManagementPeriodic(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicHomeManagementResponseModel>(){
                    override fun onSuccess(t: PeriodicHomeManagementResponseModel) {
                        if (t.code == 200){
                            getHomePeriodicManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicHomeManagementResponseModel::class.java
                                )
                                getHomePeriodicManagementModel.value = error
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

    fun getMonitorPeriodicManagement(projectCode: String, startDate:String, endDate:String){
        compositeDisposable.add(
            repository.getMonitorManagementPeriodic(projectCode, startDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicMonitorManagementResponseModel>(){
                    override fun onSuccess(t: PeriodicMonitorManagementResponseModel) {
                        if (t.code == 200){
                            getMonitorPeriodicManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicMonitorManagementResponseModel::class.java
                                )
                                getMonitorPeriodicManagementModel.value = error
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

    fun getCalendarPeriodicManagement(projectCode: String, date: String, page: Int, perPage: Int){
        compositeDisposable.add(
            repository.getCalendarManagementPeriodic(projectCode, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicCalendarManagementResponseModel>(){
                    override fun onSuccess(t: PeriodicCalendarManagementResponseModel) {
                        if (t.code == 200){
                            getCalendarPeriodicManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicCalendarManagementResponseModel::class.java
                                )
                                getCalendarPeriodicManagementModel.value = error
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

    fun getCalendarEventManagement(projectCode: String, month:String, year: String){
        compositeDisposable.add(
            repository.getCalendarEventManagementPeriodic(projectCode, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicCalendarEventResponseModel>(){
                    override fun onSuccess(t: PeriodicCalendarEventResponseModel) {
                        if (t.code == 200){
                            getCalendarEventPeriodicModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicCalendarEventResponseModel::class.java
                                )
                                getCalendarEventPeriodicModel.value = error
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

    fun getDetailPeriodicManagement(idJobs: Int){
        compositeDisposable.add(
            repository.getDetailManagamentPeriodic(idJobs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicDetailResponseModel>(){
                    override fun onSuccess(t: PeriodicDetailResponseModel) {
                        if (t.code == 200){
                            getDetailPeriodicManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicDetailResponseModel::class.java
                                )
                                getDetailPeriodicManagementModel.value = error
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

    fun getByStatusPeriodicManagement(projectCode: String, startDate: String, endDate: String, filterBy: String, page: Int, perPage: Int,locationId : Int){
        compositeDisposable.add(
            repository.getByStatusManagementPeriodic(projectCode, startDate, endDate, filterBy, page, perPage,locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicByStatusResponseModel>(){
                    override fun onSuccess(t: PeriodicByStatusResponseModel) {
                        if (t.code == 200){
                            getByStatusPeriodicManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicByStatusResponseModel::class.java
                                )
                                getByStatusPeriodicManagementModel.value = error
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

    fun putApproveJobManagements(idJobs: Int, adminMasterId: Int){
        compositeDisposable.add(
            repository.putApproveJobsManagement(idJobs, adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodApproveJobMgmntResponseModel>(){
                    override fun onSuccess(t: PeriodApproveJobMgmntResponseModel) {
                        putApproveJobsManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodApproveJobMgmntResponseModel::class.java
                                )
                                putApproveJobsManagementModel.value = error
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

    fun putCreateBaPeriodicManagement(idJobs: Int, adminMasterId: Int){
        compositeDisposable.add(
            repository.putCreateBaManagement(idJobs, adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodCreateBaManagementResponseModel>(){
                    override fun onSuccess(t: PeriodCreateBaManagementResponseModel) {
                        if (t.code == 200){
                            putCreateBaManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodCreateBaManagementResponseModel::class.java
                                )
                                putCreateBaManagementModel.value = error
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

    fun getEscallationManagement(projectCode: String){
        compositeDisposable.add(
            repository.getEscallationApproveManagement(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PeriodicEscallationResponseModel>(){
                    override fun onSuccess(t: PeriodicEscallationResponseModel) {
                        if (t.code == 200){
                            getEscallationManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PeriodicEscallationResponseModel::class.java
                                )
                                getEscallationManagementModel.value = error
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

    //for activity

    fun getEscallationManagementViewModel(): MutableLiveData<PeriodicEscallationResponseModel>{
        return getEscallationManagementModel
    }

    fun putCreateBaPeriodicManagementViewModel(): MutableLiveData<PeriodCreateBaManagementResponseModel>{
        return putCreateBaManagementModel
    }

    fun putApproveJobManagementViewModel(): MutableLiveData<PeriodApproveJobMgmntResponseModel>{
        return putApproveJobsManagementModel
    }
    fun getByStatusPeriodicManagementViewModel(): MutableLiveData<PeriodicByStatusResponseModel>{
        return getByStatusPeriodicManagementModel
    }

    fun getDetailPeriodicCalendarManagementViewModel(): MutableLiveData<PeriodicDetailResponseModel>{
        return getDetailPeriodicManagementModel
    }

    fun getPeriodicCalendarEventManagemntViewModel(): MutableLiveData<PeriodicCalendarEventResponseModel>{
        return getCalendarEventPeriodicModel
    }

    fun getPeriodicCalendarManagementViewModel(): MutableLiveData<PeriodicCalendarManagementResponseModel>{
        return getCalendarPeriodicManagementModel
    }

    fun getPeriodicMonitorManagementViewModel(): MutableLiveData<PeriodicMonitorManagementResponseModel>{
        return getMonitorPeriodicManagementModel
    }

    fun getPeriodicHomeManagementViewModel(): MutableLiveData<PeriodicHomeManagementResponseModel>{
        return getHomePeriodicManagementModel
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}