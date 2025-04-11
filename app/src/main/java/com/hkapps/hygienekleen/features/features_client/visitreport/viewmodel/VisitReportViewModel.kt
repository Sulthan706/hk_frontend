package com.hkapps.hygienekleen.features.features_client.visitreport.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.visitreport.data.repository.VisitReportRepository
import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.MainVisitReportResponseModel
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

class VisitReportViewModel(application: Application): AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    //val mutablelive
    val getMainVisitReportReponse = MutableLiveData<MainVisitReportResponseModel>()

    @Inject
    lateinit var repository: VisitReportRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }
    //viewmodel
    fun getMainVisitReport(projectCode:String, date:String, page:Int){
        compositeDisposable.add(
            repository.getMainVisitReport(projectCode, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MainVisitReportResponseModel>(){
                    override fun onSuccess(t: MainVisitReportResponseModel) {
                        if (t.code == 200){
                            getMainVisitReportReponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    MainVisitReportResponseModel::class.java
                                )
                                getMainVisitReportReponse.value = error

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

    //viewmodel for activity
    fun getMainVisitReportViewModel():MutableLiveData<MainVisitReportResponseModel>{
        return getMainVisitReportReponse
    }


    override fun onCleared() {
        compositeDisposable.dispose()
    }
}