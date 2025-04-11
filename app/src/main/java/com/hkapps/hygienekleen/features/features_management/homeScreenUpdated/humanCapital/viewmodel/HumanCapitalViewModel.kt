package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.repository.HumanCapitalRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.BranchesHumanCapitalResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.ManPowerBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.ManPowerManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.submitRating.SubmitRatingResponse
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

class HumanCapitalViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val manPowerManagementResponse = MutableLiveData<ManPowerManagementResponse>()
    val branchesHumanCapitalResponse = MutableLiveData<BranchesHumanCapitalResponse>()
    val manPowerBodResponse = MutableLiveData<ManPowerBodResponse>()
    val submitRatingResponse = MutableLiveData<SubmitRatingResponse>()

    @Inject
    lateinit var repository: HumanCapitalRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getManPowerManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getManPowerManagement(userId, keywords, filter, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ManPowerManagementResponse>() {
                    override fun onSuccess(t: ManPowerManagementResponse) {
                        manPowerManagementResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ManPowerManagementResponse::class.java
                                )
                                manPowerManagementResponse.value = error
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

    fun getBranchesHumanCapital(
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getBranchesHumanCapital(page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BranchesHumanCapitalResponse>() {
                    override fun onSuccess(t: BranchesHumanCapitalResponse) {
                        branchesHumanCapitalResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BranchesHumanCapitalResponse::class.java
                                )
                                branchesHumanCapitalResponse.value = error
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

    fun getManPowerBod(
        branchCode: String,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getManPowerBod(branchCode, keywords, filter, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ManPowerBodResponse>() {
                    override fun onSuccess(t: ManPowerBodResponse) {
                        manPowerBodResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ManPowerBodResponse::class.java
                                )
                                manPowerBodResponse.value = error
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

    fun submitRating(
        ratingByUserId: Int,
        roleUser: String,
        employeeId: Int,
        rating: Int,
        feedback: String,
        projectCode: String,
        jobCode: String
    ) {
        compositeDisposable.add(
            repository.submitRating(ratingByUserId, roleUser, employeeId, rating, feedback, projectCode, jobCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitRatingResponse>() {
                    override fun onSuccess(t: SubmitRatingResponse) {
                        submitRatingResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitRatingResponse::class.java
                                )
                                submitRatingResponse.value = error
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