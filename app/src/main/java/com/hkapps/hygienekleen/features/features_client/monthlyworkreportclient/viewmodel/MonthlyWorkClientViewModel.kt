package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.repository.MonthlyWorkClientRepository
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient.CalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.datesrkbclient.DatesRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.detailjobrkbclient.DetailJobRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.eventcalendarrkbclient.EventCalendarRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.homerkbclient.HomeRkbClientResponseModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientResponseModel
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

class MonthlyWorkClientViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val getHomeRkbClientModel = MutableLiveData<HomeRkbClientResponseModel>()
    private val getDatesRkbClientModel = MutableLiveData<DatesRkbClientResponseModel>()
    private val getEventRkbClientModel = MutableLiveData<EventCalendarRkbClientResponseModel>()
    private val getCalendarRkbClientModel = MutableLiveData<CalendarRkbClientResponseModel>()
    private val getDetailRkbClientModel = MutableLiveData<DetailJobRkbClientResponseModel>()
    private val getListByStatsRkbClient = MutableLiveData<ListByStatsRkbClientResponseModel>()

    @Inject
    lateinit var repository: MonthlyWorkClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getHomeRkbClient(projectCode: String){
        compositeDisposable.add(
            repository.getHomeRkbClient(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HomeRkbClientResponseModel>(){
                    override fun onSuccess(t: HomeRkbClientResponseModel) {
                        getHomeRkbClientModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    HomeRkbClientResponseModel::class.java
                                )
                                getHomeRkbClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDatesRkbClient(projectCode: String, startDate:String, endDate:String){
        compositeDisposable.add(
            repository.getDatesRkbClient(projectCode, startDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DatesRkbClientResponseModel>(){
                    override fun onSuccess(t: DatesRkbClientResponseModel) {
                        getDatesRkbClientModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DatesRkbClientResponseModel::class.java
                                )
                                getDatesRkbClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getEventCalendarRkbClient(projectCode: String, clientId: Int, month: String, year: String){
        compositeDisposable.add(
            repository.getEventCalendarRkbClient(projectCode, clientId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EventCalendarRkbClientResponseModel>(){
                    override fun onSuccess(t: EventCalendarRkbClientResponseModel) {
                        getEventRkbClientModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EventCalendarRkbClientResponseModel::class.java
                                )
                                getEventRkbClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getCalendarRkbClient(clientId: Int, projectCode: String, date: String, page: Int, perPage: Int){
        compositeDisposable.add(
            repository.getCalendarRkbClient(clientId, projectCode, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CalendarRkbClientResponseModel>(){
                    override fun onSuccess(t: CalendarRkbClientResponseModel) {
                        getCalendarRkbClientModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CalendarRkbClientResponseModel::class.java
                                )
                                getCalendarRkbClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailJobRkbClient(idJobs: Int){
        compositeDisposable.add(
            repository.getDetailRkbClient(idJobs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailJobRkbClientResponseModel>(){
                    override fun onSuccess(t: DetailJobRkbClientResponseModel) {
                        getDetailRkbClientModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailJobRkbClientResponseModel::class.java
                                )
                                getDetailRkbClientModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListByStatsRkbClient(clientId: Int, projectCode: String, startDate: String, endDate: String, filterBy: String, page: Int, perPage: Int,locationId : Int){
        compositeDisposable.add(
            repository.getListByStatsRkbClient(clientId, projectCode, startDate, endDate, filterBy, page, perPage,locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListByStatsRkbClientResponseModel>(){
                    override fun onSuccess(t: ListByStatsRkbClientResponseModel) {
                        getListByStatsRkbClient.value = t
                    }

                    override fun onError(e: Throwable) {
                        when(e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListByStatsRkbClientResponseModel::class.java
                                )
                                getListByStatsRkbClient.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }


    //viewmodel for activity

    fun getListByStatsRkbClientViewModel(): MutableLiveData<ListByStatsRkbClientResponseModel>{
        return getListByStatsRkbClient
    }

    fun getDetailJobRkbClientViewModel(): MutableLiveData<DetailJobRkbClientResponseModel>{
        return getDetailRkbClientModel
    }


    fun getCalendarRkbClientViewModel(): MutableLiveData<CalendarRkbClientResponseModel>{
        return getCalendarRkbClientModel
    }

    fun getEventCalendarRkbClientViewModel():MutableLiveData<EventCalendarRkbClientResponseModel>{
        return getEventRkbClientModel
    }

    fun getDatesRkbClientViewModel(): MutableLiveData<DatesRkbClientResponseModel>{
        return getDatesRkbClientModel
    }

    fun getHomeRkbClient(): MutableLiveData<HomeRkbClientResponseModel>{
        return getHomeRkbClientModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}