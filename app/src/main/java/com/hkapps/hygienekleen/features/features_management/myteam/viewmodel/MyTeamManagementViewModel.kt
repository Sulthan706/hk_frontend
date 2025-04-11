package com.hkapps.hygienekleen.features.features_management.myteam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.myteam.data.repository.MyTeamManagementRepository
import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.ListBranchMyTeamResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.ListChiefSpvMyTeamManagementResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.ListManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.ListProjectManagementResponse
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

class MyTeamManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val listProjectModel = MutableLiveData<ListProjectManagementResponse>()
    private val listChiefModel = MutableLiveData<ListChiefSpvMyTeamManagementResponse>()
    val listBranchModel = MutableLiveData<ListBranchMyTeamResponse>()
    val listManagementModel = MutableLiveData<ListManagementResponseModel>()
    val searchListPorjectModel = MutableLiveData<ListProjectManagementResponse>()

    @Inject
    lateinit var repository: MyTeamManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListProjectManagement(branchCode: String, page: Int, perPage: Int) {
        compositeDisposable.add(
            repository.getListProjectManagement(branchCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectManagementResponse>() {
                    override fun onSuccess(t: ListProjectManagementResponse) {
                        if (t.code == 200) {
                            listProjectModel.value = t
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
                                    ListProjectManagementResponse::class.java
                                )
                                listProjectModel.value = error
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

    fun getListChiefManagement(projectId: String) {
        compositeDisposable.add(
            repository.getListChiefManagement(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListChiefSpvMyTeamManagementResponse>() {
                    override fun onSuccess(t: ListChiefSpvMyTeamManagementResponse) {
                        if (t.code == 200) {
                            listChiefModel.value = t
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
                                    ListChiefSpvMyTeamManagementResponse::class.java
                                )
                                listChiefModel.value = error
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

    fun getListBranchMyTeam() {
        compositeDisposable.add(
            repository.getListBranch()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListBranchMyTeamResponse>() {
                    override fun onSuccess(t: ListBranchMyTeamResponse) {
                        if (t.code == 200) {
                            listBranchModel.value = t
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
                                    ListBranchMyTeamResponse::class.java
                                )
                                listBranchModel.value = error
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

    fun getListManagement(projectCode: String) {
        compositeDisposable.add(
            repository.getListManagement(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListManagementResponseModel>() {
                    override fun onSuccess(t: ListManagementResponseModel) {
                        if (t.code == 200) {
                            listManagementModel.value = t
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
                                    ListManagementResponseModel::class.java
                                )
                                listManagementModel.value = error
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

    fun getSearchListProject(page: Int, branchCode: String, keywords: String, perPage: Int) {
        compositeDisposable.add(
            repository.searchListProject(page, branchCode, keywords, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectManagementResponse>() {
                    override fun onSuccess(t: ListProjectManagementResponse) {
                        if (t.code == 200) {
                            searchListPorjectModel.value = t
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
                                    ListProjectManagementResponse::class.java
                                )
                                searchListPorjectModel.value = error
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

    fun getListProjectManagementResponse(): MutableLiveData<ListProjectManagementResponse> {
        return listProjectModel
    }

    fun getListChiefManagementResponse(): MutableLiveData<ListChiefSpvMyTeamManagementResponse> {
        return listChiefModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}