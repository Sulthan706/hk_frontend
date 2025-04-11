package com.hkapps.hygienekleen.features.features_vendor.service.resign.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.repository.ResignRepository
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.detailresignvendor.DetailReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.getdatevendor.DateResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.ListResignVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.submitresign.SubmitResignVendorResponseModel
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

class ResignViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val getListResignVendorModel = MutableLiveData<ListResignVendorResponseModel>()
    private val postSubmitResignVendorModel = MutableLiveData<SubmitResignVendorResponseModel>()
    private val getDateResignVendorModel = MutableLiveData<DateResignResponseModel>()
    private val getDetailResignReasonModel = MutableLiveData<DetailReasonResignResponseModel>()

    @Inject
    lateinit var repository: ResignRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListResignVendor(employeeId: Int){
        compositeDisposable.add(
            repository.getListResignVendor(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListResignVendorResponseModel>(){
                    override fun onSuccess(t: ListResignVendorResponseModel) {
                        getListResignVendorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListResignVendorResponseModel::class.java
                                )
                                getListResignVendorModel.value = error
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

    fun postSubmitResignVendor(employeeId: Int, tanggalPengajuan: String){
        compositeDisposable.add(
            repository.postSubmitResignVendor(employeeId, tanggalPengajuan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitResignVendorResponseModel>(){
                    override fun onSuccess(t: SubmitResignVendorResponseModel) {
                        postSubmitResignVendorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitResignVendorResponseModel::class.java
                                )
                                postSubmitResignVendorModel.value = error
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

    fun getDateResignVendor(projectCode: String){
        compositeDisposable.add(
            repository.getDateResignVendor(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DateResignResponseModel>(){
                    override fun onSuccess(t: DateResignResponseModel) {
                        getDateResignVendorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DateResignResponseModel::class.java
                                )
                                getDateResignVendorModel.value = error
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

    fun getDetailReasonResign(idTurnOver: Int){
        compositeDisposable.add(
            repository.getDetailResignVendor(idTurnOver)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailReasonResignResponseModel>(){
                    override fun onSuccess(t: DetailReasonResignResponseModel) {
                        getDetailResignReasonModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailReasonResignResponseModel::class.java
                                )
                                getDetailResignReasonModel.value = error
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


    //activity

    fun getDetailReasonResignViewModel(): MutableLiveData<DetailReasonResignResponseModel>{
        return getDetailResignReasonModel
    }

    fun getDateResignVendorViewModel(): MutableLiveData<DateResignResponseModel>{
        return getDateResignVendorModel
    }

    fun postSubmitResignVendorViewModel(): MutableLiveData<SubmitResignVendorResponseModel>{
        return postSubmitResignVendorModel
    }
    fun getListResignVendorViewModel(): MutableLiveData<ListResignVendorResponseModel>{
        return getListResignVendorModel
    }


}