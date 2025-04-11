package com.hkapps.hygienekleen.features.features_vendor.damagereport.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.repository.DamageReportVendorRepository
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.checkusertechnician.CheckUserVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.detailbakvendor.DetailBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.uploadbakvendor.UploadBakVendorResponseModel
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

class DamageReportVendorViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

     @Inject
     lateinit var repository: DamageReportVendorRepository

     private val listBakVendorModel = MutableLiveData<ListBakVendorResponseModel>()
     private val detailBakVendorModel = MutableLiveData<DetailBakVendorResponseModel>()
     private val uploadBakVendorModel = MutableLiveData<UploadBakVendorResponseModel>()
     private val checkUserTechnicianModel = MutableLiveData<CheckUserVendorResponseModel>()

    val getListBakMachine = MutableLiveData<BakMachineResponse>()
     init {
         DaggerAppComponent.create().injectRepository(this)
     }

    fun getListBakVendor(projectCode: String, date: String, page: Int){
        compositeDisposable.add(
            repository.getListBakVendor(projectCode, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListBakVendorResponseModel>(){
                    override fun onSuccess(t: ListBakVendorResponseModel) {
                        listBakVendorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListBakVendorResponseModel::class.java
                            )
                            listBakVendorModel.value = error
                        } else {
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }

                })
        )
    }

    fun getDetailBakVendor(idDetailBakMesin: Int){
        compositeDisposable.add(
            repository.getDetailBakVendor(idDetailBakMesin)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailBakVendorResponseModel>(){
                    override fun onSuccess(t: DetailBakVendorResponseModel) {
                        detailBakVendorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DetailBakVendorResponseModel::class.java
                            )
                            detailBakVendorModel.value = error
                        } else {
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }

                })
        )
    }

    fun putUploadBakVendor(idDetailBakMesin: Int, employeeId: Int, file: MultipartBody.Part, keteranganBak: String){
        compositeDisposable.add(
            repository.putUploadBakVendor(idDetailBakMesin, employeeId, file, keteranganBak)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UploadBakVendorResponseModel>(){
                    override fun onSuccess(t: UploadBakVendorResponseModel) {
                        uploadBakVendorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UploadBakVendorResponseModel::class.java
                            )
                            uploadBakVendorModel.value = error
                        } else {
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }

                })
        )
    }

    fun getCheckUserTechnician(employeeId: Int){
        compositeDisposable.add(
            repository.getCheckUserTechnician(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckUserVendorResponseModel>(){
                    override fun onSuccess(t: CheckUserVendorResponseModel) {
                        checkUserTechnicianModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                CheckUserVendorResponseModel::class.java
                            )
                            checkUserTechnicianModel.value = error
                        } else {
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
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



    //activity call

    fun getCheckUserVendorViewModel(): MutableLiveData<CheckUserVendorResponseModel>{
        return checkUserTechnicianModel
    }

    fun putUploadBakVendorViewModel(): MutableLiveData<UploadBakVendorResponseModel>{
        return uploadBakVendorModel
    }

    fun getDetailBakVendorViewModel(): MutableLiveData<DetailBakVendorResponseModel>{
        return detailBakVendorModel
    }

    fun getListBakVendorViewModel(): MutableLiveData<ListBakVendorResponseModel>{
        return listBakVendorModel
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}