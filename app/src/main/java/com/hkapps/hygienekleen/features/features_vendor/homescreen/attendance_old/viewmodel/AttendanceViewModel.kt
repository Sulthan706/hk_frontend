package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class AttendanceViewModel(application: Application) : AndroidViewModel(application)  {
    val chooseStaffResponseModel = MutableLiveData<ChooseStaffResponseModel>()
    val dailyActResponseModel = MutableLiveData<DailyActResponseModel>()

    val historyResponseModel = MutableLiveData<HistoryResponseModel>()
    val historyByDateResponseModel = MutableLiveData<HistoryByDateResponseModel>()

    val attStatus = MutableLiveData<AttendanceStatusResponseModel>()

    private val qrModel = MutableLiveData<QRCodeResponseModel>()
    private val postSelfie = MutableLiveData<SelfiePostResponseModel>()
    private val compositeDisposable = CompositeDisposable()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    @Inject
    lateinit var repository: com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.repository.AttendanceRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getChooseAttendanceStaff(params: String) {
            compositeDisposable.add(
                repository.getChooseStaff(params)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ChooseStaffResponseModel>() {
                        override fun onSuccess(t: ChooseStaffResponseModel) {
                            chooseStaffResponseModel.value = t
                        }

                        override fun onError(e: Throwable) {
                            if (e is HttpException) {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ChooseStaffResponseModel::class.java
                                )
                                chooseStaffResponseModel.value = error
                            }else{
                                Toast.makeText(
                                    context,
                                    "Terjadi Kesalahan.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            )
    }


    fun getDailyAct(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getDailyAct(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyActResponseModel>() {
                    override fun onSuccess(t: DailyActResponseModel) {
                        dailyActResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DailyActResponseModel::class.java
                            )
                            dailyActResponseModel.value = error
                            Log.d("DailyAct", "onError: $error", )
                        }else{
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                })
        )
    }

    fun getQRCodeViewModel(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getQRCode(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<QRCodeResponseModel>() {
                    override fun onSuccess(t: QRCodeResponseModel) {
                        qrModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                QRCodeResponseModel::class.java
                            )
                            qrModel.value = error
                        }else{
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                })
        )
    }

    fun postAttendance(employeeId: Int, projectCode: String, barcodeKey: String, imageSelfie: MultipartBody.Part) {
        compositeDisposable.add(
            repository.postImageSelfie(employeeId, projectCode, barcodeKey, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SelfiePostResponseModel>() {
                    override fun onSuccess(t: SelfiePostResponseModel) {
                        postSelfie.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                SelfiePostResponseModel::class.java
                            )
                            postSelfie.value = error
                        }else{
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        )
    }


    fun putAttendance(employeeId: Int, projectCode: String, barcodeKey: String, imageSelfie: MultipartBody.Part) {
        compositeDisposable.add(
            repository.putImageSelfie(employeeId, projectCode, barcodeKey, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SelfiePostResponseModel>() {
                    override fun onSuccess(t: SelfiePostResponseModel) {
                        postSelfie.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                SelfiePostResponseModel::class.java
                            )
                            postSelfie.value = error
                        }else{
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        )
    }


    fun getStatusAttendance(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getStatusAttendance(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceStatusResponseModel>() {
                    override fun onSuccess(t: AttendanceStatusResponseModel) {
                        attStatus.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(errorBody?.string(), AttendanceStatusResponseModel::class.java)
                            attStatus.value = error
                            Log.d("Viewmodelattendance", "onError: ")
                        }else{
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Viewmodelattendance", "onError_else: ")
                        }
                    }
                })
        )
    }

    fun getHistoryAttendance(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getHistory(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryResponseModel>() {
                    override fun onSuccess(t: HistoryResponseModel) {
                        historyResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                HistoryResponseModel::class.java
                            )
                            historyResponseModel.value = error
                            Log.d("History Attendance", "onError: $error", )
                        }else{
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        )
    }


    fun getHistoryAttendanceByDate(employeeId: Int, projectCode: String, datePrefix: String, dateSuffix: String) {
        compositeDisposable.add(
            repository.getHistoryByDate(employeeId, projectCode, datePrefix, dateSuffix)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryByDateResponseModel>() {
                    override fun onSuccess(t: HistoryByDateResponseModel) {
                        historyByDateResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                HistoryByDateResponseModel::class.java
                            )
                            historyByDateResponseModel.value = error
                            Log.d("History Att By Date", "onError: $error", )
                        }else{
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        )
    }

    fun getQRModel(): MutableLiveData<QRCodeResponseModel> {
        return qrModel
    }

    fun getSelfie(): MutableLiveData<SelfiePostResponseModel> {
        return postSelfie
    }
    override fun onCleared() {
        compositeDisposable.dispose()
    }

}