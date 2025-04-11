package com.hkapps.hygienekleen.features.features_client.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.home.data.repository.HomeClientRepository
import com.hkapps.hygienekleen.features.features_client.home.model.ProfileClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.badgenotif.BadgeNotifClient
import com.hkapps.hygienekleen.features.features_client.home.model.countChecklist.CountChecklistHomeClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.countComplaint.InfoComplaintClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.projectDashboard.ProjectInfoClientResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
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

class HomeClientViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val profileClientResponse = MutableLiveData<ProfileClientResponse>()
    private val infoComplaintResponse = MutableLiveData<InfoComplaintClientResponse>()
    val countChecklistResponse = MutableLiveData<CountChecklistHomeClientResponse>()
    val infoProjectResponse = MutableLiveData<ProjectInfoClientResponse>()

    val getDataMr = MutableLiveData<ItemMrResponse>()

    val getListBakMachine = MutableLiveData<BakMachineResponse>()

    val chartAbsenceStaffModel = MutableLiveData<ChartAbsenceStaffResponse>()

    val timeSheetModel = MutableLiveData<TimeSheetResponse>()

    //badgenotif
    private val getBadgeNotifModel = MutableLiveData<BadgeNotifClient>()

    @Inject
    lateinit var repository: HomeClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getProfileClient(userId: Int) {
        compositeDisposable.add(
            repository.getProfileClient(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProfileClientResponse>() {
                    override fun onSuccess(t: ProfileClientResponse) {
                        if (t.code == 200) {
                            profileClientResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProfileClientResponse::class.java
                                )
                                profileClientResponse.value = error
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

    fun getInfoComplaintClient(projectId: String, userId: Int) {
        compositeDisposable.add(
            repository.getInfoComplaintClient(projectId, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<InfoComplaintClientResponse>() {
                    override fun onSuccess(t: InfoComplaintClientResponse) {
                        if (t.code == 200) {
                            infoComplaintResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    InfoComplaintClientResponse::class.java
                                )
                                infoComplaintResponse.value = error
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

    fun getCountChecklist(projectCode: String) {
        compositeDisposable.add(
            repository.getCountChecklistClient(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountChecklistHomeClientResponse>() {
                    override fun onSuccess(t: CountChecklistHomeClientResponse) {
                        if (t.code == 200) {
                            countChecklistResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CountChecklistHomeClientResponse::class.java
                                )
                                countChecklistResponse.value = error
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

    fun getInfoProject(projectCode: String) {
        compositeDisposable.add(
            repository.getProjectInfo(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectInfoClientResponse>(){
                    override fun onSuccess(t: ProjectInfoClientResponse) {
                        infoProjectResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectInfoClientResponse::class.java
                                )
                                infoProjectResponse.value = error
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

    fun getBadgeNotifClient(clientId: Int, projectCode: String){
        compositeDisposable.add(
            repository.getCountNotifClient(clientId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BadgeNotifClient>(){
                    override fun onSuccess(t: BadgeNotifClient) {
                        if (t.code == 200){
                            getBadgeNotifModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BadgeNotifClient::class.java
                                )
                                getBadgeNotifModel.value = error
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

    fun getDataMr(
        projectCode : String,
        month : Int,
        year : Int,
        page : Int,
        size : Int
    ){
        compositeDisposable.add(
            repository.itemMr(projectCode, month, year, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ItemMrResponse>(){
                    override fun onSuccess(t: ItemMrResponse) {
                        getDataMr.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Log.d("ERROR","$e")
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ItemMrResponse::class.java
                                )
                                getDataMr.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListBakMachine(
        projectCode : String,
        page : Int,
        perPage : Int
    ){
        compositeDisposable.add(
            repository.getDataListBAKMachine(projectCode,page,perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BakMachineResponse>(){
                    override fun onSuccess(t: BakMachineResponse) {
                        getListBakMachine.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BakMachineResponse::class.java
                                )
                                getListBakMachine.value = error
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

    fun getAbsenceStaff(projectCode : String){
        compositeDisposable.add(
            repository.getDataAbsenceStaff(projectCode)
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
            repository.getDataTimeSheet(projectCode)
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

    fun getBadgeNotifResponse(): MutableLiveData<BadgeNotifClient>{
        return getBadgeNotifModel
    }

    fun getProfileClientResponse(): MutableLiveData<ProfileClientResponse> {
        return profileClientResponse
    }

    fun getInfoComplaintClientModel(): MutableLiveData<InfoComplaintClientResponse> {
        return infoComplaintResponse
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}