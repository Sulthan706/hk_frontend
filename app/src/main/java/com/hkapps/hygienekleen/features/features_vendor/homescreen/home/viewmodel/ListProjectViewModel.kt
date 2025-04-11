package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository.HomeRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject.ListProjectModel
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

class ListProjectViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val getListProjectModel = MutableLiveData<ListProjectModel>()

    @Inject
    lateinit var repository: HomeRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListProject(userId: Int) {
        compositeDisposable.add(
            repository.getListProject(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectModel>() {
                    override fun onSuccess(t: ListProjectModel) {
                        getListProjectModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectModel::class.java
                                )
                                getListProjectModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getListProject(): MutableLiveData<ListProjectModel> {
        return getListProjectModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}

