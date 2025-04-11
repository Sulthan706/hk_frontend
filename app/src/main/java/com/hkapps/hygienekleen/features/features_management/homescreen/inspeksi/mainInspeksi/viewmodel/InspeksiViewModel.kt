package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.repository.InspeksiRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.detailMeeting.DetailMeetingInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listArea.ListAreaInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.ClientsMeetingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea.ListKontrolAreaResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.ListLaporanAreaResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listMeeting.ListMeetingInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listObject.ListObjectInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.submitFormMeeting.SubmitFormMeetingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.submitLaporanArea.SubmitLaporanAreaResponse
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

class InspeksiViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val submitFormMeetingResponse = MutableLiveData<SubmitFormMeetingResponse>()
    val listMeetingResponse = MutableLiveData<ListMeetingInspeksiResponse>()
    val detailMeetingResponse = MutableLiveData<DetailMeetingInspeksiResponse>()
    val listAreaResponse = MutableLiveData<ListAreaInspeksiResponse>()
    val listObjectResponse = MutableLiveData<ListObjectInspeksiResponse>()
    val submitLaporanAreaResponse = MutableLiveData<SubmitLaporanAreaResponse>()
    val listLaporanAreaResponse = MutableLiveData<ListLaporanAreaResponse>()
    val listKontrolAreaResponse = MutableLiveData<ListKontrolAreaResponse>()
    val listClientMeetingResponse = MutableLiveData<ClientsMeetingResponse>()

    @Inject
    lateinit var repository: InspeksiRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun submitFormMeeting(
        userId: Int,
        projectCode: String,
        title: String,
        description: String,
        date: String,
        startTime: String,
        endTime: String,
        file: MultipartBody.Part,
        fileDescription:String,
        emailParticipant: ArrayList<String>,
        nameParticipant: ArrayList<String>
    ) {
        compositeDisposable.add(
            repository.submitFormMeeting(userId, projectCode, title, description, date, startTime, endTime, file, fileDescription, emailParticipant, nameParticipant)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormMeetingResponse>() {
                    override fun onSuccess(t: SubmitFormMeetingResponse) {
                        submitFormMeetingResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormMeetingResponse::class.java
                                )
                                submitFormMeetingResponse.value = error
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

    fun getListMeeting(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListMeeting(userId, projectCode, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListMeetingInspeksiResponse>() {
                    override fun onSuccess(t: ListMeetingInspeksiResponse) {
                        listMeetingResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListMeetingInspeksiResponse::class.java
                                )
                                listMeetingResponse.value = error
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

    fun getDetailMeeting(
        idMeeting: Int
    ) {
        compositeDisposable.add(
            repository.getDetailMeeting(idMeeting)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailMeetingInspeksiResponse>() {
                    override fun onSuccess(t: DetailMeetingInspeksiResponse) {
                        detailMeetingResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailMeetingInspeksiResponse::class.java
                                )
                                detailMeetingResponse.value = error
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

    fun getListAreaInspeksi() {
        compositeDisposable.add(
            repository.getListAreaInspeksi()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAreaInspeksiResponse>() {
                    override fun onSuccess(t: ListAreaInspeksiResponse) {
                        listAreaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAreaInspeksiResponse::class.java
                                )
                                listAreaResponse.value = error
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

    fun getListObjectInspeksi(
        idArea: Int
    ) {
        compositeDisposable.add(
            repository.getListObjectInspeksi(idArea)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListObjectInspeksiResponse>() {
                    override fun onSuccess(t: ListObjectInspeksiResponse) {
                        listObjectResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListObjectInspeksiResponse::class.java
                                )
                                listObjectResponse.value = error
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

    fun submitLaporanArea(
        createdBy: Int,
        projectCode: String,
        idArea: Int,
        idObject: Int,
        file: MultipartBody.Part,
        score: Int,
        description: String
    ) {
        compositeDisposable.add(
            repository.submitLaporanArea(createdBy, projectCode, idArea, idObject, file, score, description)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitLaporanAreaResponse>() {
                    override fun onSuccess(t: SubmitLaporanAreaResponse) {
                        submitLaporanAreaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitLaporanAreaResponse::class.java
                                )
                                submitLaporanAreaResponse.value = error
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

    fun getListLaporanArea(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: Int,
        date: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListLaporanArea(userId, projectCode, auditType, idArea, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListLaporanAreaResponse>() {
                    override fun onSuccess(t: ListLaporanAreaResponse) {
                        listLaporanAreaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListLaporanAreaResponse::class.java
                                )
                                listLaporanAreaResponse.value = error
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

    fun getListKontrolArea(
        userId: Int,
        projectCode: String,
        date: String,
        typeVisit: String
    ) {
        compositeDisposable.add(
            repository.getListKontrolArea(userId, projectCode, date, typeVisit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListKontrolAreaResponse>() {
                    override fun onSuccess(t: ListKontrolAreaResponse) {
                        listKontrolAreaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListKontrolAreaResponse::class.java
                                )
                                listKontrolAreaResponse.value = error
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

    fun getListClientMeeting(projectCode: String) {
        compositeDisposable.add(
            repository.getListClientMeeting(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClientsMeetingResponse>() {
                    override fun onSuccess(t: ClientsMeetingResponse) {
                        listClientMeetingResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClientsMeetingResponse::class.java
                                )
                                listClientMeetingResponse.value = error
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