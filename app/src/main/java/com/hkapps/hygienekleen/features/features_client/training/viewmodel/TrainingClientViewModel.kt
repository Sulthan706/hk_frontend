package com.hkapps.hygienekleen.features.features_client.training.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.training.data.repository.TrainingClientRepository
import com.hkapps.hygienekleen.features.features_client.training.model.listshifttraining.ListShiftTraining
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

class TrainingClientViewModel (application: Application): AndroidViewModel(application){
    private val compositeDisposable = CompositeDisposable()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listShiftResponse = MutableLiveData<ListShiftTraining>()
    @Inject
    lateinit var repository: TrainingClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListShift(projectCode: String){
        compositeDisposable.add(
            repository.getListShiftTraining(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListShiftTraining>(){
                    override fun onSuccess(t: ListShiftTraining) {
                        if (t.code == 200){
                            listShiftResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListShiftTraining::class.java
                                )
                                listShiftResponse.value = error

                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListShiftTrainingViewModel(): MutableLiveData<ListShiftTraining>{
        return listShiftResponse
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}