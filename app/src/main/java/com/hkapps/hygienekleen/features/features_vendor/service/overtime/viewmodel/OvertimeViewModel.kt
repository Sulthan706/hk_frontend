package com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.repository.OvertimeRepository
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.ListOvertimeChangeResponse.ListOvertimeChangeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.createOvertimeChange.CreateOvertimeChangeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.createOvertimeResign.CreateOvertimeResignResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.detailOvertimeChangeOpr.DetailOvertimeChangeOprResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.listOvertimeNew.OvertimeChangeNewResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.locationsOvertime.LocationsOvertimeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.operatorOvertimeChange.OperatorOvertimeChangeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.shiftOvertimeChange.ShiftOvertimeChangeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.subLocationsOvertime.SubLocationsOvertimeResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class OvertimeViewModel(application: Application) : AndroidViewModel(application) {

    val listOvertimeChangeResponseModel = MutableLiveData<ListOvertimeChangeResponseModel>()
    val shiftOvertimeChangeResponse = MutableLiveData<ShiftOvertimeChangeResponse>()
    val operatorOvertimeChangeResponse = MutableLiveData<OperatorOvertimeChangeResponse>()
    val replaceOprOvertimeChangeResponse = MutableLiveData<OperatorOvertimeChangeResponse>()
    val replacePengawasOvertimeResponse = MutableLiveData<OperatorOvertimeChangeResponse>()
    val replaceResignOvertimeResponse = MutableLiveData<OperatorOvertimeChangeResponse>()
    val createOvertimeChangeResponse = MutableLiveData<CreateOvertimeChangeResponse>()
    val detailOvertimeChangeResponse = MutableLiveData<DetailOvertimeChangeOprResponse>()
    val locationsOvertimeResponse = MutableLiveData<LocationsOvertimeResponse>()
    val subLocartionsOvertimeResponse = MutableLiveData<SubLocationsOvertimeResponse>()
    val createOvertimeResignResponse = MutableLiveData<CreateOvertimeResignResponse>()
    val createOvertimePengawasResponse = MutableLiveData<CreateOvertimeChangeResponse>()
    val locationByShiftResponse = MutableLiveData<LocationsOvertimeResponse>()
    val operatorOvertimeResignResponse = MutableLiveData<OperatorOvertimeChangeResponse>()
    val overtimeChangeNewResponse = MutableLiveData<OvertimeChangeNewResponse>()
    val filterOvertimeChangeResponse = MutableLiveData<OvertimeChangeNewResponse>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val compositeDisposable = CompositeDisposable()

    private val isNull = MutableLiveData<Boolean>()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: OvertimeRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListOvertimeChange(projectId: String, employeeId: Int, page: Int) {
        compositeDisposable.add(
            repository.getOvertimeChange(projectId, employeeId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOvertimeChangeResponseModel>() {
                    override fun onSuccess(t: ListOvertimeChangeResponseModel) {
                        if (t.code == 200) {
                            listOvertimeChangeResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListOvertimeChangeResponseModel::class.java
                            )
                            listOvertimeChangeResponseModel.value = error
                            isLoading?.value = false
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailOvertimeChangeOpr(overtimeId: Int) {
        compositeDisposable.add(
            repository.getDetailOvertimeChangeOpr(overtimeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailOvertimeChangeOprResponse>() {
                    override fun onSuccess(t: DetailOvertimeChangeOprResponse) {
                        if (t.code == 200) {
                            detailOvertimeChangeResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DetailOvertimeChangeOprResponse::class.java
                            )
                            detailOvertimeChangeResponse.value = error
                            isLoading?.value = false
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getShiftOvertimeChange(projectId: String) {
        compositeDisposable.add(
            repository.getShiftOvertimeChange(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object  : DisposableSingleObserver<ShiftOvertimeChangeResponse>() {
                    override fun onSuccess(t: ShiftOvertimeChangeResponse) {
                        if (t.code == 200) {
                            shiftOvertimeChangeResponse.value = t
                        }
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
                                    ShiftOvertimeChangeResponse::class.java
                                )
                                shiftOvertimeChangeResponse.value = error
                                isLoading?.value = false
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getOperatorOvertimeChange(projectId: String, date: String, overtimeType: String, shiftId: Int, jabatan: String) {
        compositeDisposable.add(
            repository.getOperatorOvertimeChange(projectId, date, overtimeType, shiftId, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorOvertimeChangeResponse>() {
                    override fun onSuccess(t: OperatorOvertimeChangeResponse) {
                        operatorOvertimeChangeResponse.value = t
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
                                    OperatorOvertimeChangeResponse::class.java
                                )
                                operatorOvertimeChangeResponse.value = error
                                isLoading?.value = false
                            }
                        } else {
                            isLoading?.value = true
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getReplaceOprOvertimeChange(projectId: String, date: String, shiftId: Int, jobCode: String, jabatan: String) {
        compositeDisposable.add(
            repository.getReplaceOperatorOvertimeChange(projectId, date, shiftId, jobCode, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorOvertimeChangeResponse>() {
                    override fun onSuccess(t: OperatorOvertimeChangeResponse) {
                        replaceOprOvertimeChangeResponse.value = t
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
                                    OperatorOvertimeChangeResponse::class.java
                                )
                                replaceOprOvertimeChangeResponse.value = error
                                isLoading?.value = false
                            }
                        } else {
                            isLoading?.value = true
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getReplaceResignOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ) {
        compositeDisposable.add(
            repository.getReplaceResignOvertimeChange(projectId, date, shiftId, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorOvertimeChangeResponse>() {
                    override fun onSuccess(t: OperatorOvertimeChangeResponse) {
                        if (t.code == 200) {
                            replaceResignOvertimeResponse.value = t
                        }
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
                                    OperatorOvertimeChangeResponse::class.java
                                )
                                replaceResignOvertimeResponse.value = error
                                isLoading?.value = false
                            }
                        } else {
                            isLoading?.value = true
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun createOvertimeChange(
        employeeId: Int,
        operatorId: Int,
        operatorReplaceId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ) {
        compositeDisposable.add(
            repository.createOvertimeChange(employeeId, operatorId, operatorReplaceId, projectId, title, description, date, shiftId, overtimeType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object  : DisposableSingleObserver<CreateOvertimeChangeResponse>() {
                    override fun onSuccess(t: CreateOvertimeChangeResponse) {
                        createOvertimeChangeResponse.value = t
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
                                    CreateOvertimeChangeResponse::class.java
                                )
                                createOvertimeChangeResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getReplacePengawas(projectId: String, date: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getReplacePengawas(projectId, date, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorOvertimeChangeResponse>() {
                    override fun onSuccess(t: OperatorOvertimeChangeResponse) {
                        replacePengawasOvertimeResponse.value = t
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
                                    OperatorOvertimeChangeResponse::class.java
                                )
                                replacePengawasOvertimeResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getLocationOvertime(projectId: String) {
        compositeDisposable.add(
            repository.getLocationsOvertime(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LocationsOvertimeResponse>() {
                    override fun onSuccess(t: LocationsOvertimeResponse) {
                        if (t.code == 200) {
                            locationsOvertimeResponse.value = t
                        }
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
                                    LocationsOvertimeResponse::class.java
                                )
                                locationsOvertimeResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getSubLocationsOvertime(projectId: String, locationId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getSubLocationsOvertime(projectId, locationId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubLocationsOvertimeResponse>() {
                    override fun onSuccess(t: SubLocationsOvertimeResponse) {
                        if (t.code == 200) {
                            subLocartionsOvertimeResponse.value = t
                        }
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
                                    SubLocationsOvertimeResponse::class.java
                                )
                                subLocartionsOvertimeResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun createOvertimeResign(
        createdById: Int,
        employeeId: Int,
        locationId: Int,
        subLocationId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ) {
        compositeDisposable.add(
            repository.createOvertimeResign(createdById, employeeId, locationId, subLocationId, projectId, title, description, date, shiftId, overtimeType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateOvertimeResignResponse>() {
                    override fun onSuccess(t: CreateOvertimeResignResponse) {
                        createOvertimeResignResponse.value = t
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
                                    CreateOvertimeResignResponse::class.java
                                )
                                createOvertimeResignResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun createOvertimePengawas (
        createdById: Int,
        employeeId: Int,
        employeeReplaceId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ) {
        compositeDisposable.add(
            repository.createOvertimePengawas(createdById, employeeId, employeeReplaceId, projectId, title, description, date, shiftId, overtimeType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateOvertimeChangeResponse>() {
                    override fun onSuccess(t: CreateOvertimeChangeResponse) {
                        createOvertimePengawasResponse.value = t
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
                                    CreateOvertimeChangeResponse::class.java
                                )
                                createOvertimePengawasResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getLocationByShift(projectId: String, shiftId: Int, date: String) {
        compositeDisposable.add(
            repository.getLocationByShift(projectId, shiftId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LocationsOvertimeResponse>() {
                    override fun onSuccess(t: LocationsOvertimeResponse) {
                        if (t.code == 200) {
                            locationByShiftResponse.value = t
                        }
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
                                    LocationsOvertimeResponse::class.java
                                )
                                locationsOvertimeResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getEmployeeOvertimeResign(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ) {
        compositeDisposable.add(
            repository.getEmployeeOvertimeResign(projectId, date, overtimeType, shiftId, locationId, subLocationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorOvertimeChangeResponse>() {
                    override fun onSuccess(t: OperatorOvertimeChangeResponse) {
                        if (t.code == 200) {
                            operatorOvertimeResignResponse.value = t
                        }
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
                                    OperatorOvertimeChangeResponse::class.java
                                )
                                operatorOvertimeResignResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getListOvertimeChange(
        employeeId: Int,
        projectCode: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListOvertimeChange(employeeId, projectCode, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OvertimeChangeNewResponse>() {
                    override fun onSuccess(t: OvertimeChangeNewResponse) {
                        overtimeChangeNewResponse.value = t
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
                                    OvertimeChangeNewResponse::class.java
                                )
                                overtimeChangeNewResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getFilterOvertimeChange(
        employeeId: Int,
        projectCode: String,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getFilterOvertimeChange(employeeId, projectCode, jabatan, startDate, endDate, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OvertimeChangeNewResponse>() {
                    override fun onSuccess(t: OvertimeChangeNewResponse) {
                        filterOvertimeChangeResponse.value = t
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
                                    OvertimeChangeNewResponse::class.java
                                )
                                filterOvertimeChangeResponse.value = error
                            }
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getShiftOvertimeChangeModel(): MutableLiveData<ShiftOvertimeChangeResponse> {
        return shiftOvertimeChangeResponse
    }

    fun getOperatorOvertimeChangeModel(): MutableLiveData<OperatorOvertimeChangeResponse> {
        return operatorOvertimeChangeResponse
    }

    fun getReplaceOprOvertimeChangeModel(): MutableLiveData<OperatorOvertimeChangeResponse> {
        return replaceOprOvertimeChangeResponse
    }

    fun createOvertimeChangeModel(): MutableLiveData<CreateOvertimeChangeResponse> {
        return createOvertimeChangeResponse
    }

    fun detailOvertimeChangeOprModel(): MutableLiveData<DetailOvertimeChangeOprResponse> {
        return detailOvertimeChangeResponse
    }

    fun checkNull(desc: String) {
        desc.isEmpty()
    }

    fun getCheckNull(): MutableLiveData<Boolean> {
        return isNull
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}