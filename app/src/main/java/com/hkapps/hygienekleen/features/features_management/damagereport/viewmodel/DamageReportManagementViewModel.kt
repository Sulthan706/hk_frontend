package com.hkapps.hygienekleen.features.features_management.damagereport.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.damagereport.data.repository.DamageReportManagementRepository
import com.hkapps.hygienekleen.features.features_management.damagereport.model.detaildamagereport.DetailDamageReportMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport.ListDamageReportResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listprojectdamagereport.ListProjectBakResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploaddamageimagebak.PutDamageBakMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadfotodamagereport.UploadFotoBakResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadfrontimagebak.PutFrontBakMgmntReponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadketerangan.UploadKeteranganResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
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

class DamageReportManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val listDamageReportManagementModel = MutableLiveData<ListDamageReportResponseModel>()
    private val detailDamageReportManagementModel = MutableLiveData<DetailDamageReportMgmntResponseModel>()
    private val listProjectDamageReportManagementModel = MutableLiveData<ListProjectBakResponseModel>()
    private val uploadFotoDamageReportManagementModel = MutableLiveData<UploadFotoBakResponseModel>()
    private val uploadFrontBakManagementModel = MutableLiveData<PutFrontBakMgmntReponseModel>()
    private val uploadDamageBakManagementModel = MutableLiveData<PutDamageBakMgmntResponseModel>()
    private val uploadKeteranganBakManagementModel = MutableLiveData<UploadKeteranganResponseModel>()

    val getListBakMachine = MutableLiveData<BakMachineResponse>()

    @Inject
    lateinit var repository: DamageReportManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListDamageReportMgmnt(adminMasterId: Int,projectCode : String, date: String, filter: String, page: Int){
        compositeDisposable.add(
            repository.getListDamageReportManagement(adminMasterId, projectCode,date, filter, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListDamageReportResponseModel>(){
                    override fun onSuccess(t: ListDamageReportResponseModel) {
                        listDamageReportManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListDamageReportResponseModel::class.java
                                )
                                listDamageReportManagementModel.value = error
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

    fun getDetailDamageReportManagement(idDetailBakMesin: Int){
        compositeDisposable.add(
            repository.getDetailDamageReportManagement(idDetailBakMesin)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailDamageReportMgmntResponseModel>(){
                    override fun onSuccess(t: DetailDamageReportMgmntResponseModel) {
                        detailDamageReportManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailDamageReportMgmntResponseModel::class.java
                                )
                                detailDamageReportManagementModel.value = error
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

    fun getListProjectBakReport(adminMasterId: Int, page: Int, keywords: String){
        compositeDisposable.add(
            repository.getListProjectDamageReportManagement(adminMasterId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectBakResponseModel>(){
                    override fun onSuccess(t: ListProjectBakResponseModel) {
                        listProjectDamageReportManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectBakResponseModel::class.java
                                )
                                listProjectDamageReportManagementModel.value = error
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

    fun uploadFotoBakManagement(idDetailBakMesin: Int, adminMasterId: Int, file1: MultipartBody.Part, file2: MultipartBody.Part, keteranganAssets:String) {
        compositeDisposable.add(
            repository.putUploadBakManagement(idDetailBakMesin, adminMasterId, file1, file2, keteranganAssets)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UploadFotoBakResponseModel>(){
                    override fun onSuccess(t: UploadFotoBakResponseModel) {
                        uploadFotoDamageReportManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UploadFotoBakResponseModel::class.java
                                )
                                uploadFotoDamageReportManagementModel.value = error
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

    fun putFrontBakManagement(idDetailBakMesin: Int, adminMasterId: Int, file1: MultipartBody.Part){
        compositeDisposable.add(
            repository.putUploadFrontBakManagement(idDetailBakMesin, adminMasterId, file1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutFrontBakMgmntReponseModel>(){
                    override fun onSuccess(t: PutFrontBakMgmntReponseModel) {
                        uploadFrontBakManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PutFrontBakMgmntReponseModel::class.java
                                )
                                uploadFrontBakManagementModel.value = error
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

    fun putDamageBakManagement(idDetailBakMesin: Int, adminMasterId: Int, file2: MultipartBody.Part){
        compositeDisposable.add(
            repository.putUploadDamageBakManagement(idDetailBakMesin, adminMasterId, file2)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutDamageBakMgmntResponseModel>(){
                    override fun onSuccess(t: PutDamageBakMgmntResponseModel) {
                        uploadDamageBakManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PutDamageBakMgmntResponseModel::class.java
                                )
                                uploadDamageBakManagementModel.value = error
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

    fun uploadKeteranganBakManagement(idDetailBakMesin: Int, adminMasterId: Int, keteranganAssets: String){
        compositeDisposable.add(
                repository.putKeteranganBakManagement(idDetailBakMesin, adminMasterId, keteranganAssets)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UploadKeteranganResponseModel>(){
                    override fun onSuccess(t: UploadKeteranganResponseModel) {
                        uploadKeteranganBakManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UploadKeteranganResponseModel::class.java
                                )
                                uploadKeteranganBakManagementModel.value = error
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




//    for activity
    fun putUploadKeteranganBakViewModel(): MutableLiveData<UploadKeteranganResponseModel>{
        return uploadKeteranganBakManagementModel
    }

    fun putUploadDamageBakViewModel(): MutableLiveData<PutDamageBakMgmntResponseModel>{
        return uploadDamageBakManagementModel
    }

    fun putUploadFrontBakViewModel(): MutableLiveData<PutFrontBakMgmntReponseModel>{
        return uploadFrontBakManagementModel
    }

    fun putUploadFotoBakViewModel(): MutableLiveData<UploadFotoBakResponseModel>{
        return uploadFotoDamageReportManagementModel
    }

    fun getListProjectDamageReportMgmntViewModel(): MutableLiveData<ListProjectBakResponseModel>{
        return listProjectDamageReportManagementModel
    }

    fun getDetailDamageReportMgmntViewModel(): MutableLiveData<DetailDamageReportMgmntResponseModel>{
        return detailDamageReportManagementModel
    }

    fun getListDamageReportMgmntViewModel(): MutableLiveData<ListDamageReportResponseModel>{
        return listDamageReportManagementModel
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}