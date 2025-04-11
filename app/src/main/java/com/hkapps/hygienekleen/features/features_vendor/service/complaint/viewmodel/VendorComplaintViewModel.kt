package com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.repository.VendorComplaintRepository
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.dashboardctalkvendor.DashboardCtalkVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.HistoryComplaintResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class VendorComplaintViewModel(application: Application) : AndroidViewModel(application) {
    private val clientComplaintModel = MutableLiveData<VendorComplaintResponseModel>()
    val clientComplaintAreaResponseModel = MutableLiveData<VendorComplaintAreaResponseModel>()
    val clientComplaintSubAreaResponseModel = MutableLiveData<VendorComplaintSubAreaResponseModel>()
    val clientComplaintHistoryResponseModel = MutableLiveData<HistoryComplaintResponseModel>()
    val clientComplaintDetailHistoryResponseModel = MutableLiveData<DetailHistoryComplaintResponse>()
    val clientDashboardComplaintModel = MutableLiveData<DashboardCtalkVendorResponseModel>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDispossable = CompositeDisposable()

    private val isNull = MutableLiveData<Boolean>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: VendorComplaintRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getDetailHistoryComplaint(complaintId: Int) {
        compositeDispossable.add(
            repository.getDetailHistoryComplaint(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailHistoryComplaintResponse>() {
                    override fun onSuccess(t: DetailHistoryComplaintResponse) {
                        clientComplaintDetailHistoryResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), DetailHistoryComplaintResponse::class.java)
                                clientComplaintDetailHistoryResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getHistoryComplaint(page: Int, projectId: String, complaintType: String) {
        compositeDispossable.add(
            repository.getHistoryComplaint(page, projectId, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryComplaintResponseModel>() {
                    override fun onSuccess(t: HistoryComplaintResponseModel) {
                        clientComplaintHistoryResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), HistoryComplaintResponseModel::class.java)
                                clientComplaintHistoryResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun postComplaintVM(
        userId: Int,
        projectId: String,
        title: String,
        description: String,
//        date: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part
    ) {
        compositeDispossable.add(
            repository.postComplaint(userId,projectId,title,description,locationId,subLocationId,image)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VendorComplaintResponseModel>() {
                    override fun onSuccess(t: VendorComplaintResponseModel) {
                        clientComplaintModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("complaint", "Error complaint")
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), VendorComplaintResponseModel::class.java)
                                clientComplaintModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }


    fun getAreaComplaint(projectId: String) {
        compositeDispossable.add(
            repository.getAreaComplaint(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VendorComplaintAreaResponseModel>() {
                    override fun onSuccess(t: VendorComplaintAreaResponseModel) {
                        clientComplaintAreaResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("complaint", "Error complaint")
                        if (e is HttpException) {
                            if (e.response()!!.code() == 401 || e.response()!!.code() == 403){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), VendorComplaintAreaResponseModel::class.java)
                                clientComplaintAreaResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun getSubAreaComplaint(projectId: String, locationId: Int) {
        compositeDispossable.add(
            repository.getSubAreaComplaint(projectId, locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VendorComplaintSubAreaResponseModel>() {
                    override fun onSuccess(t: VendorComplaintSubAreaResponseModel) {
                        clientComplaintSubAreaResponseModel.value = t
                        isLoading?.value = false
                    }
                    override fun onError(e: Throwable) {
                        Log.e("complaint", "Error complaint")
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), VendorComplaintSubAreaResponseModel::class.java)
                                clientComplaintSubAreaResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun getDashboardComplaint(projectId: String){
        compositeDispossable.add(
            repository.getDashboardComplaint(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DashboardCtalkVendorResponseModel>(){
                    override fun onSuccess(t: DashboardCtalkVendorResponseModel) {
                        clientDashboardComplaintModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), DashboardCtalkVendorResponseModel::class.java)
                                clientDashboardComplaintModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDashboardComplaintVendorViewModel(): MutableLiveData<DashboardCtalkVendorResponseModel>{
        return clientDashboardComplaintModel
    }

    fun complaintObs(): MutableLiveData<VendorComplaintResponseModel> {
        return clientComplaintModel
    }
    fun checkNull(title: String, desc: String){
        isNull.value = title.isEmpty() || desc.isEmpty()
    }

    fun getTitle(): MutableLiveData<Boolean> {
        return isNull
    }

    override fun onCleared() {
        super.onCleared()
        compositeDispossable.dispose()
    }


}