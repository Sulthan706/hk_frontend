package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.StatusAttendanceTimeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository.HomeRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen.StatusAbsenResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen.new_.StatusAbsenResponseModelNew
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject


class StatusAbsenViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val statusAbsen = MutableLiveData<StatusAbsenResponseModelNew>()
    private val statusAbsenMid = MutableLiveData<StatusAbsenResponseModel>()
    private val statusAbsenFail = MutableLiveData<Int?>()

    private val statusAttendanceTime = MutableLiveData<StatusAttendanceTimeResponseModel>()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: HomeRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getStatusAbsen(userId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getStatusAbsen(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatusAbsenResponseModelNew>() {
                    override fun onSuccess(t: StatusAbsenResponseModelNew) {
                        statusAbsen.value = t
                    }
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatusAbsenResponseModelNew::class.java
                            )
                            statusAbsen.value = error
                            Log.d("Profile", "onError: $error")
                        } else {
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


    fun statusAbsenModel(): MutableLiveData<StatusAbsenResponseModelNew> {
        return statusAbsen
    }

    fun statusAbsenModelFail(): MutableLiveData<Int?> {
        return statusAbsenFail
    }

    fun statusTIMEInOut(userId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getTimeInOut(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatusAttendanceTimeResponseModel>() {
                    override fun onSuccess(t: StatusAttendanceTimeResponseModel) {
                        statusAttendanceTime.value = t
                    }
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatusAttendanceTimeResponseModel::class.java
                            )
                            statusAttendanceTime.value = error
                            Log.d("Profile", "onError: $error")
                        } else {
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
    fun statusAbsenModelMid(userId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getStatusAbsenMid(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatusAbsenResponseModel>() {
                    override fun onSuccess(t: StatusAbsenResponseModel) {
                        statusAbsenMid.value = t
                    }
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatusAbsenResponseModel::class.java
                            )
                            statusAbsenMid.value = error
                            Log.d("Profile", "onError: $error")
                        } else {
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

    fun statusAbsenModelMid(): MutableLiveData<StatusAbsenResponseModel> {
        return statusAbsenMid
    }

    fun statusAttendanceTime(): MutableLiveData<StatusAttendanceTimeResponseModel> {
        return statusAttendanceTime
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}