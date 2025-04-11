package com.hkapps.hygienekleen.features.features_management.service.resign.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.service.resign.data.repository.ResignManagementRepository
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ListReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ListResignManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.submitresign.SubmitResignResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ResignManagementViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private var getListResignManagementModel = MutableLiveData<ListResignManagementResponseModel>()
    private var getListReasonResignModel = MutableLiveData<ListReasonResignResponseModel>()
    private var submitResignModel = MutableLiveData<SubmitResignResponseModel>()

    @Inject
    lateinit var repository: ResignManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListResignManagement(adminMasterId: Int, page: Int, pageSize: Int) {
        compositeDisposable.add(
            repository.getListResignManagement(adminMasterId, page, pageSize)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListResignManagementResponseModel>(){
                    override fun onSuccess(t: ListResignManagementResponseModel) {
                        getListResignManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListResignManagementResponseModel::class.java
                                )
                                getListResignManagementModel.value = error
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

    fun getListReasonResign(type: String, page: Int, size: Int) {
        compositeDisposable.add(
            repository.getListReasonResignManagement(type, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListReasonResignResponseModel>(){
                    override fun onSuccess(t: ListReasonResignResponseModel) {
                        getListReasonResignModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListReasonResignResponseModel::class.java
                                )
                                getListReasonResignModel.value = error
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

    fun submitResignManagement(idTurnOver: Int, adminMasterId: Int, reasonId: Int, approval: String){
        compositeDisposable.add(
            repository.submitResignManagement(idTurnOver, adminMasterId, reasonId, approval)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitResignResponseModel>(){
                    override fun onSuccess(t: SubmitResignResponseModel) {
                        submitResignModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitResignResponseModel::class.java
                                )
                                submitResignModel.value = error
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


    //acivity

    fun submitResignManagementViewModel(): MutableLiveData<SubmitResignResponseModel> {
        return submitResignModel
    }

    fun getListReasonResignManagementViewModel(): MutableLiveData<ListReasonResignResponseModel> {
        return getListReasonResignModel
    }

    fun getListResignManagementViewModel(): MutableLiveData<ListResignManagementResponseModel> {
        return getListResignManagementModel
    }
    

}