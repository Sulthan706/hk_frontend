package com.hkapps.hygienekleen.features.features_management.service.permission.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.service.permission.data.repository.PermissionManagementRepository
import com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement.ApprovalPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.createPermission.PermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.detailPermission.DetailPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.historyPermissionManagement.PermissionsHistoryManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.processPermission.ProcessPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.permission.model.statusCreatePermission.ValidatePermissionManagementResponse
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

class PermissionManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val approvalPermissionModel = MutableLiveData<ApprovalPermissionManagementResponse>()
    val createPermissionModel = MutableLiveData<PermissionManagementResponse>()
    val denyPermissionModel = MutableLiveData<ProcessPermissionManagementResponse>()
    val acceptPermissionModel = MutableLiveData<ProcessPermissionManagementResponse>()
    val historyPermissionModel = MutableLiveData<PermissionsHistoryManagementResponse>()
    val filterPermissionModel = MutableLiveData<PermissionsHistoryManagementResponse>()
    val detailPermissionModel = MutableLiveData<DetailPermissionManagementResponse>()
    val uploadPhotoPermissionModel = MutableLiveData<PermissionManagementResponse>()
    val permissionValidateModel = MutableLiveData<ValidatePermissionManagementResponse>()

    @Inject
    lateinit var repository: PermissionManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getApprovalPermission (
        userId: Int,
        jabatan: String,
        page: Int,
        size : Int
    ) {
        compositeDisposable.add(
            repository.getApprovalPermission(userId, jabatan, page,size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApprovalPermissionManagementResponse>() {
                    override fun onSuccess(t: ApprovalPermissionManagementResponse) {
                        approvalPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ApprovalPermissionManagementResponse::class.java
                                )
                                approvalPermissionModel.value = error
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

    fun postPermissionManagement(
        file: MultipartBody.Part,
        requestBy: Int,
        title: String,
        description: String,
        fromDate: String,
        endDate: String
    ) {
        compositeDisposable.add(
            repository.postPermissionManagement(file, requestBy, title, description, fromDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PermissionManagementResponse>() {
                    override fun onSuccess(t: PermissionManagementResponse) {
                        createPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PermissionManagementResponse::class.java
                                )
                                createPermissionModel.value = error
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

    fun putDenyPermission(permissionId: Int) {
        compositeDisposable.add(
            repository.putDenyPermission(permissionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessPermissionManagementResponse>() {
                    override fun onSuccess(t: ProcessPermissionManagementResponse) {
                        denyPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessPermissionManagementResponse::class.java
                                )
                                denyPermissionModel.value = error
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

    fun putAcceptPermission(
        permissionId: Int,
        employeeApproveId: Int
    ) {
        compositeDisposable.add(
            repository.putAcceptPermission(permissionId, employeeApproveId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessPermissionManagementResponse>() {
                    override fun onSuccess(t: ProcessPermissionManagementResponse) {
                        acceptPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProcessPermissionManagementResponse::class.java
                                )
                                acceptPermissionModel.value = error
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

    fun getHistoryPermissionManagement(
        adminMasterId: Int,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getHistoryPermissionManagement(adminMasterId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PermissionsHistoryManagementResponse>() {
                    override fun onSuccess(t: PermissionsHistoryManagementResponse) {
                        historyPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PermissionsHistoryManagementResponse::class.java
                                )
                                historyPermissionModel.value = error
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

    fun getFilterHistoryManagement(
        adminMasterId: Int,
        startDate: String,
        endDate: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getFilterHistoryManagement(adminMasterId, startDate, endDate, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PermissionsHistoryManagementResponse>() {
                    override fun onSuccess(t: PermissionsHistoryManagementResponse) {
                        filterPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PermissionsHistoryManagementResponse::class.java
                                )
                                filterPermissionModel.value = error
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

    fun getDetailPermissionManagement(
        permissionId: Int
    ) {
        compositeDisposable.add(
            repository.getDetailPermissionManagement(permissionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailPermissionManagementResponse>() {
                    override fun onSuccess(t: DetailPermissionManagementResponse) {
                        detailPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailPermissionManagementResponse::class.java
                                )
                                detailPermissionModel.value = error
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

    fun uploadPhotoPermission(
        permissionId: Int,
        file: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.uploadPhotoPermission(permissionId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PermissionManagementResponse>() {
                    override fun onSuccess(t: PermissionManagementResponse) {
                        uploadPhotoPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PermissionManagementResponse::class.java
                                )
                                uploadPhotoPermissionModel.value = error
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

    fun getPermissionValidateManagement(userId: Int) {
        compositeDisposable.add(
            repository.getPermissionValidateManagement(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ValidatePermissionManagementResponse>() {
                    override fun onSuccess(t: ValidatePermissionManagementResponse) {
                        permissionValidateModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ValidatePermissionManagementResponse::class.java
                                )
                                permissionValidateModel.value = error
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