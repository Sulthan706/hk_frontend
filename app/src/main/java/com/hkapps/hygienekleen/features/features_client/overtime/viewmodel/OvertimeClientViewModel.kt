package com.hkapps.hygienekleen.features.features_client.overtime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.overtime.data.repository.OvertimeClientRepository
import com.hkapps.hygienekleen.features.features_client.overtime.model.createOvertime.CreateOvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeChangeClient.DetailOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeRequestClient.DetailOvertimeRequestClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listLocation.LocationOvertimeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeChangeClient.ListOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeReqClient.OvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listSubLoc.SubLocOvertimeClientResponse
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

class OvertimeClientViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val createOvertimeClientModel = MutableLiveData<CreateOvertimeReqClientResponse>()
    private val listOvertimeChangeClientModel = MutableLiveData<ListOvertimeChangeClientResponse>()
    private val detailOvertimeChangeClientModel = MutableLiveData<DetailOvertimeChangeClientResponse>()
    private val locationResponseModel = MutableLiveData<LocationOvertimeClientResponse>()
    private val subLocResponseModel = MutableLiveData<SubLocOvertimeClientResponse>()
    private val listOvertimeReqModel = MutableLiveData<OvertimeReqClientResponse>()
    private val detailOvertimeReqModel = MutableLiveData<DetailOvertimeRequestClientResponse>()

    @Inject
    lateinit var repository: OvertimeClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun createOvertimeClient(
        employeeId: Int,
        projectId: String,
        title: String,
        description: String,
        locationId: Int,
        subLocationId: Int,
        date: String,
        startAt: String,
        endAt: String,
        file: MultipartBody.Part,
        totalWorker: Int
    ) {
      compositeDisposable.add(
          repository.createOvertimeClient(employeeId, projectId, title, description, locationId, subLocationId, date, startAt, endAt, file, totalWorker)
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(object : DisposableSingleObserver<CreateOvertimeReqClientResponse>() {
                  override fun onSuccess(t: CreateOvertimeReqClientResponse) {
                      if (t.code == 200) {
                          createOvertimeClientModel.value = t
                      }
                      isLoading?.value = false
                  }
                  override fun onError(e: Throwable) {
                      when (e) {
                          is HttpException -> {
                              val errorBody = (e as HttpException).response()?.errorBody()
                              val gson = Gson()
                              val error = gson.fromJson(
                                  errorBody?.string(),
                                  CreateOvertimeReqClientResponse::class.java
                              )
                              createOvertimeClientModel.value = error
                              isLoading?.value = false
                          }
                          is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                              isConnectionTimeout.postValue(true)
                          }
                          else -> isLoading?.value = true
                      }
                  }



              })
      )
    }

    fun getListOvertimeChangeClient(projectId: String, employeeId: Int, page: Int) {
        compositeDisposable.add(
            repository.getListOvertimeChangeClient(projectId, employeeId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOvertimeChangeClientResponse>() {
                    override fun onSuccess(t: ListOvertimeChangeClientResponse) {
                        if (t.code == 200) {
                            listOvertimeChangeClientModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOvertimeChangeClientResponse::class.java
                                )
                                listOvertimeChangeClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailOvertimeChangeClient(overtimeId: Int) {
        compositeDisposable.add(
            repository.getDetailOvertimeChangeClient(overtimeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailOvertimeChangeClientResponse>() {
                    override fun onSuccess(t: DetailOvertimeChangeClientResponse) {
                        if (t.code == 200) {
                            detailOvertimeChangeClientModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailOvertimeChangeClientResponse::class.java
                                )
                                detailOvertimeChangeClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getLocationOvertimeClient(projectId: String) {
        compositeDisposable.add(
            repository.getLocationOvertimeClient(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LocationOvertimeClientResponse>() {
                    override fun onSuccess(t: LocationOvertimeClientResponse) {
                        if (t.code == 200) {
                            locationResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LocationOvertimeClientResponse::class.java
                                )
                                locationResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getSubLocOvertimeClient(projectId: String, locationId: Int) {
        compositeDisposable.add(
            repository.getSubLocOvertimeClient(projectId, locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubLocOvertimeClientResponse>() {
                    override fun onSuccess(t: SubLocOvertimeClientResponse) {
                        if (t.code == 200) {
                            subLocResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubLocOvertimeClientResponse::class.java
                                )
                                subLocResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListOvertimeReqClient(projectId: String, page: Int) {
        compositeDisposable.add(
            repository.getListOvertimeReqClient(projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OvertimeReqClientResponse>() {
                    override fun onSuccess(t: OvertimeReqClientResponse) {
                        if (t.code == 200) {
                            listOvertimeReqModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    OvertimeReqClientResponse::class.java
                                )
                                listOvertimeReqModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailOvertimeReqClient(overtimeId: Int) {
        compositeDisposable.add(
            repository.getDetailOvertimeRequestClient(overtimeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailOvertimeRequestClientResponse>() {
                    override fun onSuccess(t: DetailOvertimeRequestClientResponse) {
                        if (t.code == 200) {
                            detailOvertimeReqModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailOvertimeRequestClientResponse::class.java
                                )
                                detailOvertimeReqModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun createOvertimeReqModel(): MutableLiveData<CreateOvertimeReqClientResponse> {
        return createOvertimeClientModel
    }

    fun listOvertimeChangeClientModel(): MutableLiveData<ListOvertimeChangeClientResponse> {
        return listOvertimeChangeClientModel
    }

    fun detailOvertimeChangeClientModel(): MutableLiveData<DetailOvertimeChangeClientResponse> {
        return detailOvertimeChangeClientModel
    }

    fun locationOvertimeClientModel(): MutableLiveData<LocationOvertimeClientResponse> {
        return locationResponseModel
    }

    fun subLocOvertimeClientModel(): MutableLiveData<SubLocOvertimeClientResponse> {
        return subLocResponseModel
    }

    fun listOvertimeReqClientResponse(): MutableLiveData<OvertimeReqClientResponse> {
        return listOvertimeReqModel
    }

    fun detailOvertimeRequestClientModel(): MutableLiveData<DetailOvertimeRequestClientResponse> {
        return detailOvertimeReqModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}