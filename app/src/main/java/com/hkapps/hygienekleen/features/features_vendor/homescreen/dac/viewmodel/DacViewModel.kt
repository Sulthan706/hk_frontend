package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.repository.DacRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActNewResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.CheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.CheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.DACCheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.PutCheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class DacViewModel(application: Application) : AndroidViewModel(application) {
    val dailyActResponseModel = MutableLiveData<DailyActResponseModel>()
    val dailyActNewResponseModel = MutableLiveData<DailyActNewResponseModel>()
    val dACCheckResponseModel = MutableLiveData<DACCheckResponseModel>()
    val checkResponseModel = MutableLiveData<CheckResponseModel>()
    val checkDACResponseModel = MutableLiveData<CheckDACResponseModel>()
    val putCheckDACResponseModel = MutableLiveData<PutCheckDACResponseModel>()


    private val compositeDisposable = CompositeDisposable()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: DacRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
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
                            Log.d("DailyAct", "onError: $error")
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

    fun getDailyNewAct(employeeId: Int, projectCode: String, idDetailProject: Int) {
        compositeDisposable.add(
            repository.getDailyNewAct(employeeId, projectCode, idDetailProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyActNewResponseModel>() {
                    override fun onSuccess(t: DailyActNewResponseModel) {
                        dailyActNewResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DailyActNewResponseModel::class.java
                            )
                            dailyActNewResponseModel.value = error
                            Log.d("DailyAct", "onError: $error")
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

    fun getCountAct(employeeId: Int, projectCode: String, idDetailProject: Int) {
        compositeDisposable.add(
            repository.getCountAct(employeeId, projectCode, idDetailProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DACCheckResponseModel>() {
                    override fun onSuccess(t: DACCheckResponseModel) {
                        dACCheckResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DACCheckResponseModel::class.java
                            )
                            dACCheckResponseModel.value = error
                            Log.d("DailyAct", "onError: $error")
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

    fun getChecklistDacLowLevel(employeeId: Int, projectCode: String, idDetailProject: Int, activityId: Int, plottingId: Int) {
        compositeDisposable.add(
            repository.getChecklistDacLowLevel(employeeId, projectCode, idDetailProject, activityId, plottingId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckDACResponseModel>() {
                    override fun onSuccess(t: CheckDACResponseModel) {
                        checkDACResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                CheckDACResponseModel::class.java
                            )
                            checkDACResponseModel.value = error
                            Log.d("DailyAct", "onError: $error")
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


    fun postDACCheckLowLevel(employeeId: Int, idDetailEmployeeProject: Int, projectId: String, plottingId: Int, shiftId: Int, locationId: Int, subLocationId: Int, activityId: Int) {
        compositeDisposable.add(
            repository.postChecklistDacLow(employeeId,
                idDetailEmployeeProject,
                projectId,
                plottingId,
                shiftId,
                locationId,
                subLocationId,
                activityId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckResponseModel>() {
                    override fun onSuccess(t: CheckResponseModel) {
                        checkResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                CheckResponseModel::class.java
                            )
                            checkResponseModel.value = error
                            Log.d("DailyAct", "onError: $error")
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


    fun putDACCheckLowLevel(idDetailEmployeeProject: Int) {
        compositeDisposable.add(
            repository.putChecklistDacLow(idDetailEmployeeProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PutCheckDACResponseModel>() {
                    override fun onSuccess(t: PutCheckDACResponseModel) {
                        putCheckDACResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                PutCheckDACResponseModel::class.java
                            )
                            putCheckDACResponseModel.value = error
                            Log.d("DailyAct", "onError: $error")
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

    //activity viewmodel

    fun getDailyNewActivityViewModel(): MutableLiveData<DailyActNewResponseModel>{
        return dailyActNewResponseModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}