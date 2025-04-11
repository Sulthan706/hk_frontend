package com.hkapps.hygienekleen.features.splash.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.splash.data.repository.SplashRepositoryImpl
import com.hkapps.hygienekleen.features.splash.model.SplashModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class SplashViewModel : ViewModel() {

    private val splashModel = MutableLiveData<SplashModel>()
    private val isError = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()
    @Inject
    lateinit var repository: SplashRepositoryImpl
    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getSplashScreen() {
        compositeDisposable.add(repository.getSplashScreen()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<SplashModel>() {
                override fun onSuccess(t: SplashModel) {
                    Log.i("tes", "Success getting splash screen")
                    splashModel.value = t
                }

                override fun onError(e: Throwable) {
                    Log.e("tes", "Error getting splash screen")
                }
            })
        )
    }

    fun getSplashModel(): MutableLiveData<SplashModel> {
        return splashModel
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}