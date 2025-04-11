package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.repository.VendorComplaintInternalRepository
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.areacomplaint.ComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.areacomplaint.subarea.ComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.chemicalsComplaintInternal.ChemicalsComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.datacomplaintinternal.DataComplaintInternalResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.detailcomplaintinternal.DetailComplaintInternalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.postdatacomplaintinternal.PostDataComplaintInternalResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.processComplaintInternal.ProcessComplaintInternalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.titlecreatecomplaint.TitleCreateComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.typeJobsComplaintInternal.JobsTypeComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.uploadvisitorobject.VisitorObjectResponseModel
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

class VendorComplaintInternalViewModel(application: Application) : AndroidViewModel(application) {
    private val userComplaintInternalViewModel = MutableLiveData<DataComplaintInternalResponseModel>()
    private val detailComplaintInternalModel = MutableLiveData<DetailComplaintInternalResponse>()
    val processComplaintInternalModel = MutableLiveData<ProcessComplaintInternalResponse>()
    val beforeImageComplaintInternalModel = MutableLiveData<ProcessComplaintInternalResponse>()
    val progressImageComplaintInternalModel = MutableLiveData<ProcessComplaintInternalResponse>()
    val afterImageComplaintInternalModel = MutableLiveData<ProcessComplaintInternalResponse>()
    val submitComplaintInternalModel = MutableLiveData<ProcessComplaintInternalResponse>()
    val titleCreateComplaintInternalResponse = MutableLiveData<TitleCreateComplaintResponse>()
    val complaintAreaResponseModel = MutableLiveData<ComplaintAreaResponseModel>()
    val complaintSubAreaResponseModel = MutableLiveData<ComplaintSubAreaResponseModel>()
    val postDataComplaintInternalResponseModel = MutableLiveData<PostDataComplaintInternalResponseModel>()
    val closeComplaintInternalResponse = MutableLiveData<DetailComplaintInternalResponse>()
    val chemicalsComplaintResponse = MutableLiveData<ChemicalsComplaintResponse>()
    val jobsTypeComplaintResponse = MutableLiveData<JobsTypeComplaintResponse>()
    val putVisitorObjectModel = MutableLiveData<VisitorObjectResponseModel>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDispossable = CompositeDisposable()
    private val isNull = MutableLiveData<Boolean>()
    private val isConnectionTimeout = MutableLiveData<Boolean>()


    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: VendorComplaintInternalRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getUserComplaintInternalViewModel(projectId: String, page: Int){
        compositeDispossable.add(
            repository.getComplaintInternalRDS(projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DataComplaintInternalResponseModel>(){
                    override fun onSuccess(t: DataComplaintInternalResponseModel){
                        userComplaintInternalViewModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DataComplaintInternalResponseModel::class.java
                                )
                                userComplaintInternalViewModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                }))
    }

    fun getDetailComplaintInternal(complaintId: Int){
        compositeDispossable.add(
            repository.getDetailComplaintInternal(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailComplaintInternalResponse>(){
                    override fun onSuccess(t: DetailComplaintInternalResponse) {
                        if (t.code == 200) {
                            detailComplaintInternalModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailComplaintInternalResponse::class.java
                                )
                                detailComplaintInternalModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun putProcessComplaintInternal(complaintId: Int, employeeId: Int, comments: String, idTypeJobs: Int) {
        compositeDispossable.add(
            repository.putProcessComplaintInternal(complaintId, employeeId, comments, idTypeJobs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintInternalResponse>() {
                    override fun onSuccess(t: ProcessComplaintInternalResponse) {
                        processComplaintInternalModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessComplaintInternalResponse::class.java
                                )
                                processComplaintInternalModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }
    fun putBeforeImageComplaintInternal(complaintId: Int, employeeId: Int, beforeImage: MultipartBody.Part) {
        compositeDispossable.add(
            repository.putBeforeImageComplaintInternal(complaintId, employeeId, beforeImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintInternalResponse>() {
                    override fun onSuccess(t: ProcessComplaintInternalResponse) {
                        beforeImageComplaintInternalModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessComplaintInternalResponse::class.java
                                )
                                beforeImageComplaintInternalModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun putProgressImageComplaintInternal(complaintId: Int, employeeId: Int, progressImage: MultipartBody.Part) {
        compositeDispossable.add(
            repository.putProgressImageComplaintInternal(complaintId, employeeId, progressImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintInternalResponse>() {
                    override fun onSuccess(t: ProcessComplaintInternalResponse) {
                        progressImageComplaintInternalModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessComplaintInternalResponse::class.java
                                )
                                progressImageComplaintInternalModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun putAfterImageComplaintInternal(complaintId: Int, employeeId: Int, afterImage: MultipartBody.Part) {
        compositeDispossable.add(
            repository.putAfterImageComplaintInternal(complaintId, employeeId, afterImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintInternalResponse>() {
                    override fun onSuccess(t: ProcessComplaintInternalResponse) {
                        afterImageComplaintInternalModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessComplaintInternalResponse::class.java
                                )
                                afterImageComplaintInternalModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun putSubmitComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        reportComments: String,
        totalWorker: Int,
        idChemicals: ArrayList<Int>
    ) {
        compositeDispossable.add(
            repository.putSubmitComplaintInternal(complaintId, employeeId, reportComments, totalWorker, idChemicals)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintInternalResponse>() {
                    override fun onSuccess(t: ProcessComplaintInternalResponse) {
                        submitComplaintInternalModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessComplaintInternalResponse::class.java
                                )
                                submitComplaintInternalModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getTitleComplaint(){
        compositeDispossable.add(
            repository.getTitleCreateComplaint()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TitleCreateComplaintResponse>() {
                    override fun onSuccess(t: TitleCreateComplaintResponse) {
                        titleCreateComplaintInternalResponse.value = t
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
                                val error = gson.fromJson(errorBody?.string(), TitleCreateComplaintResponse::class.java)
                                titleCreateComplaintInternalResponse.value = error
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
                .subscribeWith(object : DisposableSingleObserver<ComplaintAreaResponseModel>() {
                    override fun onSuccess(t: ComplaintAreaResponseModel) {
                        complaintAreaResponseModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), ComplaintAreaResponseModel::class.java)
                                complaintAreaResponseModel.value = error
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
                .subscribeWith(object : DisposableSingleObserver<ComplaintSubAreaResponseModel>() {
                    override fun onSuccess(t: ComplaintSubAreaResponseModel) {
                        complaintSubAreaResponseModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), ComplaintSubAreaResponseModel::class.java)
                                complaintSubAreaResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun postComplaintInternal(
        userId: Int,
        projectId: String,
        title: Int,
        description: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        image4: MultipartBody.Part,
        idTypeJobs: Int
    ){
        compositeDispossable.add(
            repository.postComplaintInternal(userId,projectId,title,description,locationId,subLocationId,image,image2,image3,image4, idTypeJobs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PostDataComplaintInternalResponseModel>() {
                    override fun onSuccess(t: PostDataComplaintInternalResponseModel) {
                        postDataComplaintInternalResponseModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), PostDataComplaintInternalResponseModel::class.java)
                                postDataComplaintInternalResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun putCloseComplaintInternal(
        complaintId: Int
    ) {
        compositeDispossable.add(
            repository.putCloseComplaintInternal(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailComplaintInternalResponse>() {
                    override fun onSuccess(t: DetailComplaintInternalResponse) {
                        closeComplaintInternalResponse.value = t
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
                                val error = gson.fromJson(errorBody?.string(), DetailComplaintInternalResponse::class.java)
                                closeComplaintInternalResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getChemicalsComplaint() {
        compositeDispossable.add(
            repository.getChemicalsComplaint()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChemicalsComplaintResponse>() {
                    override fun onSuccess(t: ChemicalsComplaintResponse) {
                        chemicalsComplaintResponse.value = t
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
                                val error = gson.fromJson(errorBody?.string(), ChemicalsComplaintResponse::class.java)
                                chemicalsComplaintResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getJobsTypeComplaintInternal() {
        compositeDispossable.add(
            repository.getJobsTypeComplaintInternal()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<JobsTypeComplaintResponse>() {
                    override fun onSuccess(t: JobsTypeComplaintResponse) {
                        jobsTypeComplaintResponse.value = t
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
                                val error = gson.fromJson(errorBody?.string(), JobsTypeComplaintResponse::class.java)
                                jobsTypeComplaintResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putVisitorObject(employeeId: Int, complaintId: Int, idObject: Int, file: MultipartBody.Part, typeImg: String){
        compositeDispossable.add(
            repository.putVisitorObjectComplaint(employeeId, complaintId, idObject, file, typeImg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitorObjectResponseModel>(){
                    override fun onSuccess(t: VisitorObjectResponseModel) {
                        putVisitorObjectModel.value = t
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
                                val error = gson.fromJson(errorBody?.string(), VisitorObjectResponseModel::class.java)
                                putVisitorObjectModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putVisitorObjectViewModel(): MutableLiveData<VisitorObjectResponseModel>{
        return putVisitorObjectModel
    }

    fun postObs(): MutableLiveData<PostDataComplaintInternalResponseModel>{
        return postDataComplaintInternalResponseModel
    }

    fun complaintObs(): MutableLiveData<DataComplaintInternalResponseModel> {
        return userComplaintInternalViewModel
    }

    fun getListComplaintInternalResponse(): MutableLiveData<DetailComplaintInternalResponse> {
        return detailComplaintInternalModel
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

    fun checkNull(desc: String){
        isNull.value = desc.isEmpty()
    }

}
