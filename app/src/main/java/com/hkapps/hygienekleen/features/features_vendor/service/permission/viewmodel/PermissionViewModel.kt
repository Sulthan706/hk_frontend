package com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.repository.PermissionRepository
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CheckCreatePermissionResponse
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.CreatePermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.DetailPermissionResponse
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.lowlevel.HistoryPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.OperatorPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.PermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.PutDenialPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.PutPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.historyMid.HistoryPermissionMidResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class PermissionViewModel(application: Application) : AndroidViewModel(application) {
    private val createPermissionResponseModel = MutableLiveData<CreatePermissionResponseModel>()
    val historyResponseModel = MutableLiveData<HistoryPermissionResponseModel>()
    val permissionResponseModel = MutableLiveData<PermissionResponseModel>()
    val detailResponseModel = MutableLiveData<DetailPermissionResponse>()
    val operatorPermissionResponseModel = MutableLiveData<OperatorPermissionResponseModel>()
    val putPermissionResponseModel = MutableLiveData<PutPermissionResponseModel>()
    val putPermissionResponseModelNew = MutableLiveData<PutPermissionResponseModel>()
    val putDenialPermissionResponseModel = MutableLiveData<PutDenialPermissionResponseModel>()
    val putDenialPermissionResponseModelNew = MutableLiveData<PutDenialPermissionResponseModel>()
    val checkCreatePermissionModel = MutableLiveData<CheckCreatePermissionResponse>()
    val historyPermissionMidModel = MutableLiveData<HistoryPermissionMidResponse>()
    val historyDateMidModel = MutableLiveData<HistoryPermissionMidResponse>()
    val uploadPhotoPermissionModel = MutableLiveData<DetailPermissionResponse>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDispossable = CompositeDisposable()

    private val isNull = MutableLiveData<Boolean>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: PermissionRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getDetailPermission(permissionId: Int) {
        compositeDispossable.add(
            repository.getDetailPermission(permissionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<DetailPermissionResponse>() {
                    override fun onSuccess(t: DetailPermissionResponse) {
                        detailResponseModel.value = t
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
                                    DetailPermissionResponse::class.java
                                )
                                detailResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun getPermission(projectId: String, employeeId: Int, jabatan: String, page: Int) {
        compositeDispossable.add(
            repository.getPermission(projectId, employeeId, jabatan, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PermissionResponseModel>() {
                    override fun onSuccess(t: PermissionResponseModel) {
                        permissionResponseModel.value = t
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
                                    PermissionResponseModel::class.java
                                )
                                permissionResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getHistoryPermission(projectId: String, employeeId: Int, page: Int) {
        compositeDispossable.add(
            repository.getHistoryPermission(projectId, employeeId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryPermissionResponseModel>() {
                    override fun onSuccess(t: HistoryPermissionResponseModel) {
                        historyResponseModel.value = t
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
                                    HistoryPermissionResponseModel::class.java
                                )
                                historyResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putSubmitOperator(
        permissionId: Int,
        employeeApproveId: Int,
        employeeReplaceId: Int,
        projectId: String,
        date: String
    ) {
        compositeDispossable.add(
            repository.putPermission(
                permissionId, employeeApproveId, employeeReplaceId, projectId, date
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutPermissionResponseModel>() {
                    override fun onSuccess(t: PutPermissionResponseModel) {
                        putPermissionResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("permission", "Error permission")
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
                                    PutPermissionResponseModel::class.java
                                )
                                putPermissionResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun putSubmitOperatorNew(
        permissionId: Int,
        employeeApproveId: Int,
        projectId: String
    ) {
        compositeDispossable.add(
            repository.putPermissionNew(
                permissionId, employeeApproveId, projectId
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutPermissionResponseModel>() {
                    override fun onSuccess(t: PutPermissionResponseModel) {
                        putPermissionResponseModelNew.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("permission", "Error permission")
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
                                    PutPermissionResponseModel::class.java
                                )
                                putPermissionResponseModelNew.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }

    fun putDenialOperator(
        permissionId: Int
    ) {
        compositeDispossable.add(
            repository.putDenialPermission(
                permissionId
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutDenialPermissionResponseModel>() {
                    override fun onSuccess(t: PutDenialPermissionResponseModel) {
                        putDenialPermissionResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("permission", "Error permission")
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
                                    PutDenialPermissionResponseModel::class.java
                                )
                                putDenialPermissionResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }


    fun putDenialOperatorNew(
        permissionId: Int
    ) {
        compositeDispossable.add(
            repository.putDenialPermissionNew(
                permissionId
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutDenialPermissionResponseModel>() {
                    override fun onSuccess(t: PutDenialPermissionResponseModel) {
                        putDenialPermissionResponseModelNew.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("permission", "Error permission")
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
                                    PutDenialPermissionResponseModel::class.java
                                )
                                putDenialPermissionResponseModelNew.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }


    fun postPermissionVM(
        image: MultipartBody.Part,
        requestBy: Int,
        projectId: String,
        title: String,
        description: String,
        dateStart: String,
        dateEnd: String
    ) {
        compositeDispossable.add(
            repository.postPermission(
                image,
                requestBy, projectId, title, description, dateStart, dateEnd
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreatePermissionResponseModel>() {
                    override fun onSuccess(t: CreatePermissionResponseModel) {
                        createPermissionResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("permission", "Error permission")
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
                                    CreatePermissionResponseModel::class.java
                                )
                                createPermissionResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }
                })
        )
    }


    fun getOperatorPermission(projectId: String, date: String, shiftId: Int, idDetailEmployeeProject: Int) {
        compositeDispossable.add(
            repository.getOperatorPermission(projectId, date, shiftId, idDetailEmployeeProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorPermissionResponseModel>() {
                    override fun onSuccess(t: OperatorPermissionResponseModel) {
                        operatorPermissionResponseModel.value = t
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
                                    OperatorPermissionResponseModel::class.java
                                )
                                operatorPermissionResponseModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getCheckPermission(employeeId: Int) {
        compositeDispossable.add(
            repository.getCheckPermission(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckCreatePermissionResponse>() {
                    override fun onSuccess(t: CheckCreatePermissionResponse) {
                        checkCreatePermissionModel.value = t
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
                                    CheckCreatePermissionResponse::class.java
                                )
                                checkCreatePermissionModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getHistoryPermissionMid(
        employeeId: Int
    ) {
        compositeDispossable.add(
            repository.getHistoryPermissionMid(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryPermissionMidResponse>() {
                    override fun onSuccess(t: HistoryPermissionMidResponse) {
                        historyPermissionMidModel.value = t
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
                                    HistoryPermissionMidResponse::class.java
                                )
                                historyPermissionMidModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getHistoryDateMid(
        employeeId: Int,
        startDate: String,
        endDate: String
    ) {
        compositeDispossable.add(
            repository.getHistoryDateMid(employeeId, startDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryPermissionMidResponse>() {
                    override fun onSuccess(t: HistoryPermissionMidResponse) {
                        historyDateMidModel.value = t
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
                                    HistoryPermissionMidResponse::class.java
                                )
                                historyDateMidModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun uploadPhotoPermission(
        permissionId: Int,
        file: MultipartBody.Part
    ) {
        compositeDispossable.add(
            repository.uploadPhotoPermission(permissionId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailPermissionResponse>() {
                    override fun onSuccess(t: DetailPermissionResponse) {
                        uploadPhotoPermissionModel.value = t
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
                                    DetailPermissionResponse::class.java
                                )
                                uploadPhotoPermissionModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getOperatorModel(): MutableLiveData<OperatorPermissionResponseModel>{
        return operatorPermissionResponseModel
    }

    fun putOperatorModel(): MutableLiveData<PutPermissionResponseModel>{
        return putPermissionResponseModel
    }

    fun putDenialOperatorModel(): MutableLiveData<PutDenialPermissionResponseModel>{
        return putDenialPermissionResponseModel
    }

    fun putOperatorModelNew(): MutableLiveData<PutPermissionResponseModel>{
        return putPermissionResponseModelNew
    }

    fun putDenialOperatorModelNew(): MutableLiveData<PutDenialPermissionResponseModel>{
        return putDenialPermissionResponseModelNew
    }


    fun getDetailPermission(): MutableLiveData<DetailPermissionResponse>{
        return detailResponseModel
    }

    fun permissionObs(): MutableLiveData<CreatePermissionResponseModel> {
        return createPermissionResponseModel
    }

    fun checkNull(desc: String) {
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