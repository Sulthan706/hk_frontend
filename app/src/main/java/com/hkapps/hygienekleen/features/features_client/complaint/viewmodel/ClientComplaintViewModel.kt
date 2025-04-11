package com.hkapps.hygienekleen.features.features_client.complaint.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.complaint.data.repository.ClientComplaintRepository
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.CloseComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.complaintvisitorclient.ListCtalkVisitorClientResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkclient.DashboardCtalkClientResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient.DashboardCtalkVisitorResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.historyComplaint.HistoryComplaintResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.statusCreateComplaint.ValidateCreateComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.titleCreateComplaint.ListTitleCreateComplaintClientResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class ClientComplaintViewModel(application: Application) : AndroidViewModel(application) {
    private val clientComplaintModel = MutableLiveData<ClientComplaintResponseModel>()
    val clientComplaintAreaResponseModel = MutableLiveData<ClientComplaintAreaResponseModel>()
    val clientComplaintSubAreaResponseModel = MutableLiveData<ClientComplaintSubAreaResponseModel>()
    val clientComplaintHistoryResponseModel = MutableLiveData<HistoryComplaintResponseModel>()
    val clientComplaintDetailHistoryResponseModel = MutableLiveData<DetailHistoryComplaintResponse>()
    val closeComplaintResponse = MutableLiveData<CloseComplaintResponse>()
    val clientComplaintTitleResponseModel = MutableLiveData<ListTitleCreateComplaintClientResponse>()
    val validateCreateComplaintClientResponse = MutableLiveData<ValidateCreateComplaintResponse>()
    val clientCtalkVisitorModel = MutableLiveData<ListCtalkVisitorClientResponseModel>()
    val dashBoardClientCtalkModel = MutableLiveData<DashboardCtalkClientResponseModel>()
    val dashBoardClientCtalkVisitorModel = MutableLiveData<DashboardCtalkVisitorResponseModel>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDispossable = CompositeDisposable()

    private val isNull = MutableLiveData<Boolean>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: ClientComplaintRepository

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

    fun getHistoryComplaint(page: Int, projectId: String, clientId: Int, complaintType: ArrayList<String>) {
        compositeDispossable.add(
            repository.getHistoryComplaint(page, projectId, clientId, complaintType)
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
        title: Int,
        description: String,
//        date: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        image4: MultipartBody.Part
    ) {
        compositeDispossable.add(
            repository.postComplaint(userId,projectId,title,description,locationId,subLocationId,image,image2,image3,image4)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClientComplaintResponseModel>() {
                    override fun onSuccess(t: ClientComplaintResponseModel) {
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
                                val error = gson.fromJson(errorBody?.string(), ClientComplaintResponseModel::class.java)
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
                .subscribeWith(object : DisposableSingleObserver<ClientComplaintAreaResponseModel>() {
                    override fun onSuccess(t: ClientComplaintAreaResponseModel) {
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
                                val error = gson.fromJson(errorBody?.string(), ClientComplaintAreaResponseModel::class.java)
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
                .subscribeWith(object : DisposableSingleObserver<ClientComplaintSubAreaResponseModel>() {
                    override fun onSuccess(t: ClientComplaintSubAreaResponseModel) {
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
                                val error = gson.fromJson(errorBody?.string(), ClientComplaintSubAreaResponseModel::class.java)
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

    fun putCloseComplaint(complaintId: Int) {
        compositeDispossable.add(
            repository.putCloseComplaint(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CloseComplaintResponse>() {
                    override fun onSuccess(t: CloseComplaintResponse) {
                        closeComplaintResponse.value = t
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
                                val error = gson.fromJson(errorBody?.string(), CloseComplaintResponse::class.java)
                                closeComplaintResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getTitleComplaint() {
        compositeDispossable.add(
            repository.getTitleCreateComplaint()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListTitleCreateComplaintClientResponse>() {
                    override fun onSuccess(t: ListTitleCreateComplaintClientResponse) {
                        clientComplaintTitleResponseModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), ListTitleCreateComplaintClientResponse::class.java)
                                clientComplaintTitleResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getValidateCreateCtalk(projectCode: String) {
        compositeDispossable.add(
            repository.getValidateCreateCtalk(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ValidateCreateComplaintResponse>() {
                    override fun onSuccess(t: ValidateCreateComplaintResponse) {
                        validateCreateComplaintClientResponse.value = t
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
                                val error = gson.fromJson(errorBody?.string(), ValidateCreateComplaintResponse::class.java)
                                validateCreateComplaintClientResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListCtalkVisitorClient(page: Int, projectCode: String, filter: String){
        compositeDispossable.add(
            repository.getListCtalkVisitorClient(page, projectCode, filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCtalkVisitorClientResponseModel>(){
                    override fun onSuccess(t: ListCtalkVisitorClientResponseModel) {
                        clientCtalkVisitorModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), ListCtalkVisitorClientResponseModel::class.java)
                                clientCtalkVisitorModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDashboardCtalkClient(projectCode: String, beginDate:String, endDate: String){
        compositeDispossable.add(
            repository.getDashboardCtalkClient(projectCode, beginDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DashboardCtalkClientResponseModel>(){
                    override fun onSuccess(t: DashboardCtalkClientResponseModel) {
                        dashBoardClientCtalkModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), DashboardCtalkClientResponseModel::class.java)
                                dashBoardClientCtalkModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDashboardCtalkVisitorClient(projectCode: String, beginDate: String, endDate: String){
        compositeDispossable.add(
            repository.getDashboardCtalkVisitorClient(projectCode, beginDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DashboardCtalkVisitorResponseModel>(){
                    override fun onSuccess(t: DashboardCtalkVisitorResponseModel) {
                        dashBoardClientCtalkVisitorModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), DashboardCtalkVisitorResponseModel::class.java)
                                dashBoardClientCtalkVisitorModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDasboardCtalkVisitorClientViewModel(): MutableLiveData<DashboardCtalkVisitorResponseModel>{
        return dashBoardClientCtalkVisitorModel
    }

    fun getDashboardCtalkClientViewModel(): MutableLiveData<DashboardCtalkClientResponseModel>{
        return dashBoardClientCtalkModel
    }

    fun getListCtalkVisitorClientViewModel(): MutableLiveData<ListCtalkVisitorClientResponseModel>{
        return clientCtalkVisitorModel
    }

    fun complaintObs(): MutableLiveData<ClientComplaintResponseModel> {
        return clientComplaintModel
    }
    fun checkNull(desc: String){
        isNull.value = desc.isEmpty()
    }

    fun getTitle(): MutableLiveData<Boolean> {
        return isNull
    }

    override fun onCleared() {
        super.onCleared()
        compositeDispossable.dispose()
    }

}