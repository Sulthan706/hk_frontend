package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.repository.ClosingRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignmentResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ListClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift.DetailShiftResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
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

class ClosingViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val getListDiversionModel  = MutableLiveData<DiversionResponse>()
    val getListAreaAssignmentModel  = MutableLiveData<AreaAssignmentResponse>()
    val getHistoryClosingModel = MutableLiveData<HistoryClosingResponse>()

    val getShiftDiversionModel = MutableLiveData<DetailShiftResponse>()
    val submitClosingModel = MutableLiveData<ClosingResponse>()
    val checkClosingModel = MutableLiveData<ClosingResponse>()

    val listClosingModel = MutableLiveData<ListClosingResponse>()

    val generateFileClosingModel = MutableLiveData<ClosingResponse>()

    val sendEmailClosingModel = MutableLiveData<ClosingResponse>()

    val clientClosingResponse = MutableLiveData<ClientClosingResponse>()

    val detailDailyTargetChief = MutableLiveData<DailyTargetResponse>()

    val listDiversionChief = MutableLiveData<DiversionResponse>()

    val submitClosingChief = MutableLiveData<ClosingResponse>()

    val listHistoryClosingChiefModel = MutableLiveData<HistoryClosingResponse>()

    val generateFileClosingSPVModel = MutableLiveData<ClosingResponse>()

    val sendEmailClosingSPVModel = MutableLiveData<ClosingResponse>()

    @Inject
    lateinit var repository: ClosingRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListDailyDiversion(
        idDetailEmployeeProject : Int,
        locationId : Int,
        page : Int,
        perPage : Int
    ){

        compositeDisposable.add(
            repository.getListDailyDiversion(idDetailEmployeeProject,locationId, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DiversionResponse>(){
                    override fun onSuccess(t: DiversionResponse) {
                        getListDiversionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DiversionResponse::class.java
                                )
                                getListDiversionModel.value = error
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

    fun getAreaAssignment(
        projectCode : String,
        category : String,
        page : Int,
        perPage : Int
    ){

        compositeDisposable.add(
            repository.getAreaAssignment(projectCode,category, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AreaAssignmentResponse>(){
                    override fun onSuccess(t: AreaAssignmentResponse) {
                        getListAreaAssignmentModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("KUNY","$e")
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AreaAssignmentResponse::class.java
                                )
                                getListAreaAssignmentModel.value = error
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

    fun getHistoryClosing(
        employeeId: Int,startAt : String,endAt : String,page : Int,perPage : Int
    ){
        compositeDisposable.add(
            repository.getHistoryClosing(employeeId, startAt,endAt, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object  : DisposableSingleObserver<HistoryClosingResponse>(){
                    override fun onSuccess(t: HistoryClosingResponse) {
                        getHistoryClosingModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    HistoryClosingResponse::class.java
                                )
                                getHistoryClosingModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                }
        ))
    }

    fun getShiftDiversion(idShift : Int,projectCode : String,page : Int,size : Int){
        compositeDisposable.add(
            repository.getShiftDiversion(idShift, projectCode, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailShiftResponse>(){
                    override fun onSuccess(t: DetailShiftResponse) {
                        getShiftDiversionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailShiftResponse::class.java
                                )
                                getShiftDiversionModel.value = error
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

    fun submitClosing(idDetailEmployeeProject: Int,employeeId : Int){
        compositeDisposable.add(
            repository.submitDailyClosing(idDetailEmployeeProject, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        submitClosingModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClosingResponse::class.java
                                )
                                Log.d("KUNY","$error")
                                submitClosingModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                }))
    }

    fun listClosing(
        projectCode : String,
        startAt : String,
        endAt : String,
        roles : String,
        rolesTwo : String,
        status : String,
        page : Int,
        size : Int
    ){
        compositeDisposable.add(
            repository.listClosing(projectCode,startAt,endAt,roles,rolesTwo,status,page,size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListClosingResponse>(){
                    override fun onSuccess(t: ListClosingResponse) {
                        listClosingModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListClosingResponse::class.java
                                )
                                listClosingModel.value = error
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

    fun generateFileClosing(
        projectCode : String,
        date : String,
        userId : Int
    ){
        compositeDisposable.add(
            repository.generateFileClosingManagement(projectCode,date,userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        generateFileClosingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClosingResponse::class.java
                                )
                                generateFileClosingModel.value = error
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

    fun sendEmailClosing(
        sendEmailClosingRequest: SendEmailClosingRequest
    ){
        compositeDisposable.add(
            repository.sendEmailClosingManagement(sendEmailClosingRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        sendEmailClosingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClosingResponse::class.java
                                )
                                sendEmailClosingModel.value = error
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

    fun getListClientClosing(
        projectCode : String
    ){
        compositeDisposable.add(
            repository.getListClientClosing(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClientClosingResponse>(){
                    override fun onSuccess(t: ClientClosingResponse) {
                        clientClosingResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClientClosingResponse::class.java
                                )
                                clientClosingResponse.value = error
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

    fun getDailyTargetChief(
        projectCode: String,
        date : String,
        employeeId : Int
    ){
        compositeDisposable.add(
            repository.getDetailDailyTargetChief(projectCode,date,employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyTargetResponse>(){
                    override fun onSuccess(t: DailyTargetResponse) {
                        detailDailyTargetChief.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyTargetResponse::class.java
                                )
                                detailDailyTargetChief.value = error
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

    fun getDiversionChief(
        locationId: Int,
        projectCode: String,
        date: String,
        page: Int,
        size: Int
    ){
        compositeDisposable.add(
            repository.getListDiversionChief(locationId, projectCode, date, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DiversionResponse>(){
                    override fun onSuccess(t: DiversionResponse) {
                        listDiversionChief.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DiversionResponse::class.java
                                )
                                listDiversionChief.value = error
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

    fun submitClosingChief(
        projectCode: String,
        date: String,
        userId: Int
    ){

        compositeDisposable.add(
            repository.submitClosingChief(projectCode, date, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        submitClosingChief.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClosingResponse::class.java
                                )
                                submitClosingChief.value = error
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

    fun getListClosingChief(
        projectCode : String,
        startAt: String,
        endAt: String,
        page: Int,
        size: Int,
    ){
        compositeDisposable.add(
            repository.getListHistoryClosingChief(projectCode,startAt,endAt,page,size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryClosingResponse>(){
                    override fun onSuccess(t: HistoryClosingResponse) {
                        listHistoryClosingChiefModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    HistoryClosingResponse::class.java
                                )
                                listHistoryClosingChiefModel.value = error
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

    fun generateFileClosingSPV(
        projectCode : String,
        date : String,
        userId : Int
    ){
        compositeDisposable.add(
            repository.generateFileClosingSPV(projectCode,date,userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        generateFileClosingSPVModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClosingResponse::class.java
                                )
                                generateFileClosingSPVModel.value = error
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

    fun sendEmailClosingSPV(
        sendEmailClosingRequest: SendEmailClosingRequest
    ){
        compositeDisposable.add(
            repository.sendEmailClosingSPV(sendEmailClosingRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        sendEmailClosingSPVModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClosingResponse::class.java
                                )
                                sendEmailClosingSPVModel.value = error
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
