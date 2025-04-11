package com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.repository.ClosingManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.CheckStatusChiefResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyDetailTargetManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ListDailyTargetManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ClosingManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val isConnectionTimeout = MutableLiveData<Boolean>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listDailyTargetModel = MutableLiveData<ListDailyTargetManagementResponse>()

    val detailDailyTargetModel = MutableLiveData<DailyDetailTargetManagementResponse>()

    val listDiversionModel = MutableLiveData<DiversionResponse>()

    val submitClosingModel = MutableLiveData<ClosingResponse>()

    val generateFileModel = MutableLiveData<ClosingResponse>()

    val sendEmailModel = MutableLiveData<ClosingResponse>()

    val clientClosingResponse = MutableLiveData<ClientClosingResponse>()

    val divertedModelResponse = MutableLiveData<ClosingResponse>()

    val listHistoryClosingModel = MutableLiveData<HistoryClosingResponse>()

    val checkClosingChiefModel = MutableLiveData<CheckStatusChiefResponse>()

    val checkClosingStatusModel = MutableLiveData<CheckStatusChiefResponse>()


    @Inject
    lateinit var repository: ClosingManagementRepository

    init{
        DaggerAppComponent.create().injectRepository(this)
    }

    fun checkClosingChief(
        projectCode : String
    ){
        compositeDisposable.add(
            repository.checkClosingChief(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckStatusChiefResponse>(){
                    override fun onSuccess(t: CheckStatusChiefResponse) {
                        checkClosingChiefModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CheckStatusChiefResponse::class.java
                                )
                                checkClosingChiefModel.value = error
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

    fun getListDalyTarget(id : Int,date : String){
        compositeDisposable.add(
            repository.getListDailyTargetManagement(id,date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object  : DisposableSingleObserver<ListDailyTargetManagementResponse>(){
                    override fun onSuccess(t: ListDailyTargetManagementResponse) {
                        listDailyTargetModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListDailyTargetManagementResponse::class.java
                                )
                                listDailyTargetModel.value = error
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

    fun getDetailDailyTarget(id : String,date  : String){
        compositeDisposable.add(
            repository.getDetailDailyTargetManagement(id, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyDetailTargetManagementResponse>(){
                    override fun onSuccess(t: DailyDetailTargetManagementResponse) {
                        detailDailyTargetModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyDetailTargetManagementResponse::class.java
                                )
                                detailDailyTargetModel.value = error
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

    fun listDiversion(locationId : Int,projectCode : String,date : String,page : Int,size : Int){
        compositeDisposable.add(
            repository.getListDiversionManagement(locationId,projectCode, date, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DiversionResponse>(){
                    override fun onSuccess(t: DiversionResponse) {
                        listDiversionModel.value = t
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
                                listDiversionModel.value = error
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

    fun submitClosing(projectCode : String,date : String,adminMasterId : Int){
        compositeDisposable.add(
            repository.submitClosingManagement(projectCode, date, adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        submitClosingModel.value = t
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

                })
        )
    }

    fun generateFileClosingManagement(
        projectCode : String,
        date : String,
        adminMasterId : Int
    ){
        compositeDisposable.add(
            repository.generateFileClosingManagement(projectCode, date, adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        generateFileModel.value = t
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
                                generateFileModel.value = error
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

    fun sendEmailClosingManagement(
        sendEmailClosingRequest: SendEmailClosingRequest
    ){
        compositeDisposable.add(
            repository.sendEmailClosingManagement(sendEmailClosingRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        sendEmailModel.value = t
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
                                sendEmailModel.value = error
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

    fun getListClientClosingManagement(
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

    fun divertedManagement(
        idJob : Int,
        adminMasterId: Int,
        desc : String,
        date : String,
        divertedShift : Int,
        file : MultipartBody.Part
    ){
        compositeDisposable.add(
            repository.divertedManagement(idJob,adminMasterId,desc,date,divertedShift,file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClosingResponse>(){
                    override fun onSuccess(t: ClosingResponse) {
                        divertedModelResponse.value = t
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
                                divertedModelResponse.value = error
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
                        listHistoryClosingModel.value = t
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
                                listHistoryClosingModel.value = error
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

    fun checkClosingPopupHome(
        adminMasterId: Int
    ){
        compositeDisposable.add(
            repository.checkClosingStatus(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckStatusChiefResponse>(){
                    override fun onSuccess(t: CheckStatusChiefResponse) {
                        checkClosingStatusModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CheckStatusChiefResponse::class.java
                                )
                                checkClosingStatusModel.value = error
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
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}