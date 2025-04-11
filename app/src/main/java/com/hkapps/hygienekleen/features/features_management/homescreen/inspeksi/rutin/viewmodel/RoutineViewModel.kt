package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.repository.RoutineRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.detailRoutine.DetailRoutineReportResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.ClientsRoutineResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.ListRoutineVisitedResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.submitRoutine.SubmitFormRoutineResponse
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

class RoutineViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listClientResponse = MutableLiveData<ClientsRoutineResponse>()
    val submitFormRoutineResponse = MutableLiveData<SubmitFormRoutineResponse>()
    val listRoutineResponse = MutableLiveData<ListRoutineVisitedResponse>()
    val detailRoutineResponse = MutableLiveData<DetailRoutineReportResponse>()

    @Inject
    lateinit var repository: RoutineRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListClient(
        projectCode: String
    ) {
        compositeDisposable.add(
            repository.getListClient(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClientsRoutineResponse>() {
                    override fun onSuccess(t: ClientsRoutineResponse) {
                        listClientResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClientsRoutineResponse::class.java
                                )
                                listClientResponse.value = error
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

    fun submitFormRoutine(
        userId: Int,
        projectCode: String,
        title: String,
        description: String,
        date: String,
        file: MultipartBody.Part,
        fileDescription:String,
        emailParticipant: ArrayList<String>
    ) {
        compositeDisposable.add(
            repository.submitFormRoutine(userId, projectCode, title, description, date, file, fileDescription, emailParticipant)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormRoutineResponse>() {
                    override fun onSuccess(t: SubmitFormRoutineResponse) {
                        submitFormRoutineResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormRoutineResponse::class.java
                                )
                                submitFormRoutineResponse.value = error
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

    fun getListRoutine(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListRoutine(userId, projectCode, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListRoutineVisitedResponse>() {
                    override fun onSuccess(t: ListRoutineVisitedResponse) {
                        listRoutineResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListRoutineVisitedResponse::class.java
                                )
                                listRoutineResponse.value = error
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

    fun getDetailRoutine(
        idRoutine: Int
    ) {
        compositeDisposable.add(
            repository.getDetailRoutine(idRoutine)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailRoutineReportResponse>() {
                    override fun onSuccess(t: DetailRoutineReportResponse) {
                        detailRoutineResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailRoutineReportResponse::class.java
                                )
                                detailRoutineResponse.value = error
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