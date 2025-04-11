package com.hkapps.hygienekleen.features.features_client.notifcation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.notifcation.data.repository.NotifRepository
import com.hkapps.hygienekleen.features.features_client.notifcation.model.NotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ListNotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.readnotifclient.ReadNotifClientResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class NotifClientViewModel(application: Application) : AndroidViewModel(application) {
    val chooseStaffResponseModel = MutableLiveData<ChooseStaffResponseModel>()

    val historyResponseModel = MutableLiveData<HistoryResponseModel>()
    val historyByDateResponseModel = MutableLiveData<HistoryByDateResponseModel>()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    val shift = MutableLiveData<AttendanceShiftStaffNotAttendanceResponseModel>()
    val employeeSchResponseModel = MutableLiveData<EmployeeSchResponseModel>()


    val notifClientModel = MutableLiveData<NotifClientResponseModel>()
    //list
    val listNotifClientModel = MutableLiveData<ListNotifClientResponseModel>()
    //readnotif
    val putReadNotifClientModel = MutableLiveData<ReadNotifClientResponseModel>()

    private val compositeDisposable = CompositeDisposable()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: NotifRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getNotifClient(clientId: Int, page: Int) {
        compositeDisposable.add(
            repository.getNotifClient(clientId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotifClientResponseModel>() {
                    override fun onSuccess(t: NotifClientResponseModel) {
                        notifClientModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotifClientResponseModel::class.java
                            )
                            notifClientModel.value = error
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

    fun listNotifClient(clientId: Int, projectId: String, page: Int){
        compositeDisposable.add(
            repository.getListNotifClient(clientId, projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListNotifClientResponseModel>(){
                    override fun onSuccess(t: ListNotifClientResponseModel) {
                        if (t.code == 200){
                            listNotifClientModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListNotifClientResponseModel::class.java
                            )
                            listNotifClientModel.value = error
                        } else {
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

    fun putReadNotifClient(notificationId: Int, clientId: Int){
        compositeDisposable.add(
            repository.putReadNotifClient(notificationId, clientId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReadNotifClientResponseModel>(){
                    override fun onSuccess(t: ReadNotifClientResponseModel) {
                        if (t.code == 200){
                            putReadNotifClientModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ReadNotifClientResponseModel::class.java
                            )
                            putReadNotifClientModel.value = error
                        } else {
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

    fun putReadNotifClient(): MutableLiveData<ReadNotifClientResponseModel>{
        return putReadNotifClientModel
    }

    fun getListNotifClient(): MutableLiveData<ListNotifClientResponseModel>{
        return listNotifClientModel
    }

    fun getNotificationResponseModel(): MutableLiveData<NotifClientResponseModel> {
        return notifClientModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}