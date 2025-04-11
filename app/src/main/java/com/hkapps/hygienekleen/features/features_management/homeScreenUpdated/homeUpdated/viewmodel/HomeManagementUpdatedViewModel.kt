package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.repository.HomeManagementUpdateRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.lastVisit.VisitHomeManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.RemainingVisitsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.RemainingVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitTeknisi.RemainingVisitsTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.rkbHome.RkbHomeManagementResponse
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

class HomeManagementUpdatedViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    // bod ceo
    val rkbHomeBodResponse = MutableLiveData<RkbHomeManagementResponse>()
    val lastVisitHomeBodResponse = MutableLiveData<VisitHomeManagementResponse>()
    val listRemainingVisitBodResponse = MutableLiveData<RemainingVisitsBodResponse>()

    // management
    val rkbHomeManagementResponse = MutableLiveData<RkbHomeManagementResponse>()
    val lastVisitHomeManagementResponse = MutableLiveData<VisitHomeManagementResponse>()
    val listRemainingVisitManagementResponse = MutableLiveData<RemainingVisitsManagementResponse>()

    // teknisi
    val rkbHomeTeknisiResponse = MutableLiveData<RkbHomeManagementResponse>()
    val lastVisitHomeTeknisiResponse = MutableLiveData<VisitHomeManagementResponse>()
    val listRemainingVisitTeknisiResponse = MutableLiveData<RemainingVisitsTeknisiResponse>()

    @Inject
    lateinit var repository: HomeManagementUpdateRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getRkbBodHome(
        bodId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getRkbBodHome(bodId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RkbHomeManagementResponse>() {
                    override fun onSuccess(t: RkbHomeManagementResponse) {
                        rkbHomeBodResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RkbHomeManagementResponse::class.java
                                )
                                rkbHomeBodResponse.value = error
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

    fun getLastVisitBod(
        adminMasterId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getLastVisitBod(adminMasterId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitHomeManagementResponse>() {
                    override fun onSuccess(t: VisitHomeManagementResponse) {
                        lastVisitHomeBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    VisitHomeManagementResponse::class.java
                                )
                                lastVisitHomeBodResponse.value = error
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

    fun getListRemainingVisitBod(
        bodId: Int,
        date: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getListRemainingVisitBod(bodId, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RemainingVisitsBodResponse>() {
                    override fun onSuccess(t: RemainingVisitsBodResponse) {
                        listRemainingVisitBodResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RemainingVisitsBodResponse::class.java
                                )
                                listRemainingVisitBodResponse.value = error
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

    fun getRkbManagementHome(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getRkbManagementHome(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RkbHomeManagementResponse>() {
                    override fun onSuccess(t: RkbHomeManagementResponse) {
                        rkbHomeManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RkbHomeManagementResponse::class.java
                                )
                                rkbHomeManagementResponse.value = error
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

    fun getLastVisitManagement(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getLastVisitManagement(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitHomeManagementResponse>() {
                    override fun onSuccess(t: VisitHomeManagementResponse) {
                        lastVisitHomeManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    VisitHomeManagementResponse::class.java
                                )
                                lastVisitHomeManagementResponse.value = error
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

    fun getListRemainingVisitManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getListRemainingVisitManagement(userId, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RemainingVisitsManagementResponse>() {
                    override fun onSuccess(t: RemainingVisitsManagementResponse) {
                        listRemainingVisitManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RemainingVisitsManagementResponse::class.java
                                )
                                listRemainingVisitManagementResponse.value = error
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

    fun getRkbTeknisiHome(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getRkbTeknisiHome(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RkbHomeManagementResponse>() {
                    override fun onSuccess(t: RkbHomeManagementResponse) {
                        rkbHomeTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RkbHomeManagementResponse::class.java
                                )
                                rkbHomeTeknisiResponse.value = error
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

    fun getLastVisitTeknisi(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getLastVisitTeknisi(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitHomeManagementResponse>() {
                    override fun onSuccess(t: VisitHomeManagementResponse) {
                        lastVisitHomeTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    VisitHomeManagementResponse::class.java
                                )
                                lastVisitHomeTeknisiResponse.value = error
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

    fun getListRemainingVisitTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getListRemainingVisitTeknisi(userId, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RemainingVisitsTeknisiResponse>() {
                    override fun onSuccess(t: RemainingVisitsTeknisiResponse) {
                        listRemainingVisitTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RemainingVisitsTeknisiResponse::class.java
                                )
                                listRemainingVisitTeknisiResponse.value = error
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
        compositeDisposable.dispose()
    }
}