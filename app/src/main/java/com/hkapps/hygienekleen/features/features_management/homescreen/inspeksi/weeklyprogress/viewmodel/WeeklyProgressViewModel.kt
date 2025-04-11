package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel

import android.util.Log
import com.hkapps.hygienekleen.di.DaggerAppComponent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.repository.WeeklyProgressRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.createweekly.CreateWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.detailweekly.DetailWeeklyResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.ListWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.validation.ValidationResponse
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

class WeeklyProgressViewModel : ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    val listWeeklyProgressResponse = MutableLiveData<ListWeeklyProgressResponse>()
    val checkValidationModel = MutableLiveData<ValidationResponse>()
    val detailWeeklyResponse = MutableLiveData<DetailWeeklyResponse>()
    val createWeeklyProgressResponse = MutableLiveData<CreateWeeklyProgressResponse>()
    val updateWeeklyProgressResponse = MutableLiveData<CreateWeeklyProgressResponse>()

    @Inject
    lateinit var repository: WeeklyProgressRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListWeeklyProgress(
        projectCode: String,
        adminMasterId: Int,
        startAt: String,
        endAt : String,
        status : String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListWeeklyProgress(projectCode, adminMasterId, startAt,endAt,status, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListWeeklyProgressResponse>() {
                    override fun onSuccess(t: ListWeeklyProgressResponse) {
                        listWeeklyProgressResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListWeeklyProgressResponse::class.java
                                )
                                listWeeklyProgressResponse.value = error
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

    fun checkValidation(adminMasterId : Int,date : String){
        compositeDisposable.add(
            repository.checkValidation(adminMasterId,date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ValidationResponse>(){
                    override fun onSuccess(t: ValidationResponse) {
                        Log.d("TESTEDR","$t")
                        checkValidationModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TESTEDX","$e")
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ValidationResponse::class.java
                                )
                                checkValidationModel.value = error
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

    fun getDetailWeeklyProgress(idWeekly: Int) {
        compositeDisposable.add(
            repository.getDetailWeeklyProgress(idWeekly)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailWeeklyResponse>() {
                    override fun onSuccess(t: DetailWeeklyResponse) {
                        detailWeeklyResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailWeeklyResponse::class.java
                                )
                                detailWeeklyResponse.value = error
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

    fun createWeeklyProgress(
        adminMasterId: Int,
        projectCode: String,
        location: String,
        materialType: String,
        chemical: String,
        volumeChemical: Int,
        cleaningMethod: String,
        frequency: Int,
        areaSize: Int,
        totalPic: Int,
        status: String,
        fileBefore: MultipartBody.Part,
        fileAfter: MultipartBody.Part?
    ) {
        compositeDisposable.add(
            repository.createWeeklyProgress(
                adminMasterId,
                projectCode,
                location,
                materialType,
                chemical,
                volumeChemical,
                cleaningMethod,
                frequency,
                areaSize,
                totalPic,
                fileBefore,
                fileAfter,
                status,
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateWeeklyProgressResponse>(){
                    override fun onSuccess(t: CreateWeeklyProgressResponse) {
                        createWeeklyProgressResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateWeeklyProgressResponse::class.java
                                )
                                createWeeklyProgressResponse.value = error
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

    fun updateWeeklyProgress(
        idWeekly: Int,
        adminMasterId: Int,
        projectCode: String,
        location: String,
        materialType: String,
        chemical: String,
        volumeChemical: Int,
        cleaningMethod: String,
        frequency: Int,
        areaSize: Int,
        totalPic: Int,
        status: String,
        fileBefore: MultipartBody.Part,
        fileAfter: MultipartBody.Part?
    ) {
        compositeDisposable.add(
            repository.updateWeeklyProgress(
                idWeekly,
                adminMasterId,
                projectCode,
                location,
                materialType,
                chemical,
                volumeChemical,
                cleaningMethod,
                frequency,
                areaSize,
                totalPic,
                fileBefore,
                fileAfter,
                status,
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateWeeklyProgressResponse>(){
                    override fun onSuccess(t: CreateWeeklyProgressResponse) {
                        Log.d("KIBOYYY","$t")
                        updateWeeklyProgressResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("KIBOYYYY","$e")
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateWeeklyProgressResponse::class.java
                                )
                                updateWeeklyProgressResponse.value = error
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