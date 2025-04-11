package com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.shareloc.data.repository.ShareLocManagementRepository
import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.GetAllShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.detailmainsharelocmanagement.DetailShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.eventcalenderdetailmanagement.EventCalenderManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.GetListSearchAllMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.sharelocmanagement.PutShareLocManagementResponseModel
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

class ShareLocManagementViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val putShareLocManagementModel = MutableLiveData<PutShareLocManagementResponseModel>()
    val getAllShareLocManagementModel = MutableLiveData<GetAllShareLocMgmntResponseModel>()
    val getListSearchAllManagementModel = MutableLiveData<GetListSearchAllMgmntResponseModel>()
    val getDetailShareLocManagementModel = MutableLiveData<DetailShareLocMgmntResponseModel>()
    val getEventCalenderManagementModel = MutableLiveData<EventCalenderManagementResponseModel>()

    //livedata location and address
    private val locationData = MutableLiveData<Location>()
    private val addressData = MutableLiveData<String>()

    @Inject
    lateinit var repository: ShareLocManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun updateAddress(address: String){
        addressData.value = address
    }
    fun getAddressData(): LiveData<String>{
        return addressData
    }
    fun updateLocation(location: Location) {
        locationData.value = location
    }
    fun getLocationData(): LiveData<Location> {
        return locationData
    }

    fun putShareLocManagement(managementId: Int, latitude: Double, longitude: Double, address: String){
        compositeDisposable.add(
            repository.putShareLocManagement(managementId, latitude, longitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutShareLocManagementResponseModel>(){
                    override fun onSuccess(t: PutShareLocManagementResponseModel) {
                        if (t.code == 200){
                            putShareLocManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PutShareLocManagementResponseModel::class.java
                                )
                                putShareLocManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getAllShareLocManagement(){
        compositeDisposable.add(
            repository.getAllShareLocManagement()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GetAllShareLocMgmntResponseModel>(){
                    override fun onSuccess(t: GetAllShareLocMgmntResponseModel) {
                        getAllShareLocManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    GetAllShareLocMgmntResponseModel::class.java
                                )
                                getAllShareLocManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getListAllSearchManagement(page: Int, keywords: String){
        compositeDisposable.add(
            repository.getListSearchAllManagement(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GetListSearchAllMgmntResponseModel>(){

                    override fun onSuccess(t: GetListSearchAllMgmntResponseModel) {
                        getListSearchAllManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    GetListSearchAllMgmntResponseModel::class.java
                                )
                                getListSearchAllManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getDetailManagementShareLoc(managementId: Int, date: String){
        compositeDisposable.add(
            repository.getDetailManagementShareLoc(managementId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailShareLocMgmntResponseModel>(){
                    override fun onSuccess(t: DetailShareLocMgmntResponseModel) {
                        getDetailShareLocManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailShareLocMgmntResponseModel::class.java
                                )
                                getDetailShareLocManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getEventCalenderManagement(managementId: Int, month: String, year: String){
        compositeDisposable.add(
            repository.getEventCalendarManagement(managementId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EventCalenderManagementResponseModel>(){
                    override fun onSuccess(t: EventCalenderManagementResponseModel) {
                        getEventCalenderManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EventCalenderManagementResponseModel::class.java
                                )
                                getEventCalenderManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }


    //call for activity
    fun getEventCalendarManagementViewModel(): MutableLiveData<EventCalenderManagementResponseModel>{
        return getEventCalenderManagementModel
    }

    fun getDetailManagementShareLocViewModel(): MutableLiveData<DetailShareLocMgmntResponseModel>{
        return getDetailShareLocManagementModel
    }

    fun getListAllSearchManagemntViewModel(): MutableLiveData<GetListSearchAllMgmntResponseModel>{
        return getListSearchAllManagementModel
    }

    fun getAllShareLocManagementViewModel(): MutableLiveData<GetAllShareLocMgmntResponseModel>{
        return getAllShareLocManagementModel
    }

    fun putShareLocManagementViewModel(): MutableLiveData<PutShareLocManagementResponseModel>{
        return putShareLocManagementModel
    }



    override fun onCleared() {
        compositeDisposable.dispose()
    }
}