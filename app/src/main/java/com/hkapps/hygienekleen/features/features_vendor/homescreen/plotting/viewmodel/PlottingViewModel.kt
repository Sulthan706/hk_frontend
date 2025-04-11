package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.repository.PlottingRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.model.PlottingResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class PlottingViewModel: ViewModel() {
    val plottingResponseModel = MutableLiveData<PlottingResponseModel>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: PlottingRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getPlotting(params: String) {
        compositeDisposable.add(
            repository.getPlotting(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PlottingResponseModel>() {
                    override fun onSuccess(t: PlottingResponseModel) {
                        plottingResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                plottingResponseModel::class.java
                            )
//                            plottingResponseModel.value = error
                        }
                    }
                })
        )
    }


    override fun onCleared() {
        compositeDisposable.dispose()
    }
}