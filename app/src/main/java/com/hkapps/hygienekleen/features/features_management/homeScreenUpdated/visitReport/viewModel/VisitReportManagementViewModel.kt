package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.repository.VisitReportManagementRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.DailyVisitBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement.DailyVisitManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.DailyVisitTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.eventCalendar.EventCalendarManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport.PlannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.UnplannedVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport.VisitReportManagementResponse
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

class VisitReportManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val visitReportBodResponse = MutableLiveData<VisitReportManagementResponse>()
    val visitReportManagementResponse = MutableLiveData<VisitReportManagementResponse>()
    val visitReportTeknisiResponse = MutableLiveData<VisitReportManagementResponse>()

    val plannedVisitsBodResponse = MutableLiveData<PlannedVisitsManagementResponse>()
    val plannedVisitsManagementResponse = MutableLiveData<PlannedVisitsManagementResponse>()
    val plannedVisitsTeknisiResponse = MutableLiveData<PlannedVisitsManagementResponse>()

    val unplannedVisitsBodResponse = MutableLiveData<UnplannedVisitsManagementResponse>()
    val unplannedVisitsManagementResponse = MutableLiveData<UnplannedVisitsManagementResponse>()
    val unplannedVisitsTeknisiResponse = MutableLiveData<UnplannedVisitsManagementResponse>()

    val eventCalendarBodResponse = MutableLiveData<EventCalendarManagementResponse>()
    val eventCalendarManagementResponse = MutableLiveData<EventCalendarManagementResponse>()
    val eventCalendarTeknisiResponse = MutableLiveData<EventCalendarManagementResponse>()

    val dailyVisitBodResponse = MutableLiveData<DailyVisitBodResponse>()
    val dailyVisitManagementResponse = MutableLiveData<DailyVisitManagementResponse>()
    val dailyVisitTeknisiResponse = MutableLiveData<DailyVisitTeknisiResponse>()

    @Inject
    lateinit var repository: VisitReportManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getVisitReportBod(
        userId: Int,
        date: String,
        date2: String
    ) {
        compositeDisposable.add(
            repository.getVisitReportBod(userId, date, date2)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitReportManagementResponse>(){
                    override fun onSuccess(t: VisitReportManagementResponse) {
                        visitReportBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    VisitReportManagementResponse::class.java
                                )
                                visitReportBodResponse.value = error
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

    fun getVisitReportManagement(
        userId: Int,
        date: String,
        date2: String
    ) {
        compositeDisposable.add(
            repository.getVisitReportManagement(userId, date, date2)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitReportManagementResponse>() {
                    override fun onSuccess(t: VisitReportManagementResponse) {
                        visitReportManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    VisitReportManagementResponse::class.java
                                )
                                visitReportManagementResponse.value = error
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

    fun getVisitReportTeknisi(
        userId: Int,
        date: String,
        date2: String
    ) {
        compositeDisposable.add(
            repository.getVisitReportTeknisi(userId, date, date2)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VisitReportManagementResponse>() {
                    override fun onSuccess(t: VisitReportManagementResponse) {
                        visitReportTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    VisitReportManagementResponse::class.java
                                )
                                visitReportTeknisiResponse.value = error
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

    fun getPlannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getPlannedVisitsBod(userId, date, date2, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PlannedVisitsManagementResponse>() {
                    override fun onSuccess(t: PlannedVisitsManagementResponse) {
                        plannedVisitsBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PlannedVisitsManagementResponse::class.java
                                )
                                plannedVisitsBodResponse.value = error
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

    fun getPlannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getPlannedVisitsManagement(userId, date, date2, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PlannedVisitsManagementResponse>() {
                    override fun onSuccess(t: PlannedVisitsManagementResponse) {
                        plannedVisitsManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PlannedVisitsManagementResponse::class.java
                                )
                                plannedVisitsManagementResponse.value = error
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

    fun getPlannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getPlannedVisitsTeknisi(userId, date, date2, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PlannedVisitsManagementResponse>() {
                    override fun onSuccess(t: PlannedVisitsManagementResponse) {
                        plannedVisitsTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PlannedVisitsManagementResponse::class.java
                                )
                                plannedVisitsTeknisiResponse.value = error
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

    fun getUnplannedVisitsBod(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getUnplannedVisitsBod(userId, date, date2, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UnplannedVisitsManagementResponse>() {
                    override fun onSuccess(t: UnplannedVisitsManagementResponse) {
                        unplannedVisitsBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UnplannedVisitsManagementResponse::class.java
                                )
                                unplannedVisitsBodResponse.value = error
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

    fun getUnplannedVisitsManagement(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getUnplannedVisitsManagement(userId, date, date2, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UnplannedVisitsManagementResponse>() {
                    override fun onSuccess(t: UnplannedVisitsManagementResponse) {
                        unplannedVisitsManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UnplannedVisitsManagementResponse::class.java
                                )
                                unplannedVisitsManagementResponse.value = error
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

    fun getUnplannedVisitsTeknisi(
        userId: Int,
        date: String,
        date2: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getUnplannedVisitsTeknisi(userId, date, date2, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UnplannedVisitsManagementResponse>() {
                    override fun onSuccess(t: UnplannedVisitsManagementResponse) {
                        unplannedVisitsTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UnplannedVisitsManagementResponse::class.java
                                )
                                unplannedVisitsTeknisiResponse.value = error
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

    fun getEventCalendarBod(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getEventCalendarBod(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EventCalendarManagementResponse>() {
                    override fun onSuccess(t: EventCalendarManagementResponse) {
                        eventCalendarBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EventCalendarManagementResponse::class.java
                                )
                                eventCalendarBodResponse.value = error
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

    fun getEventCalendarManagement(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getEventCalendarManagement(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EventCalendarManagementResponse>() {
                    override fun onSuccess(t: EventCalendarManagementResponse) {
                        eventCalendarManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EventCalendarManagementResponse::class.java
                                )
                                eventCalendarManagementResponse.value = error
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

    fun getEventCalendarTeknisi(
        userId: Int,
        month: Int,
        year: Int
    ) {
        compositeDisposable.add(
            repository.getEventCalendarTeknisi(userId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EventCalendarManagementResponse>() {
                    override fun onSuccess(t: EventCalendarManagementResponse) {
                        eventCalendarTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EventCalendarManagementResponse::class.java
                                )
                                eventCalendarTeknisiResponse.value = error
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

    fun getDailyVisitsBod(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getDailyVisitsBod(userId, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyVisitBodResponse>() {
                    override fun onSuccess(t: DailyVisitBodResponse) {
                        dailyVisitBodResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyVisitBodResponse::class.java
                                )
                                dailyVisitBodResponse.value = error
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

    fun getDailyVisitsManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getDailyVisitsManagement(userId, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyVisitManagementResponse>() {
                    override fun onSuccess(t: DailyVisitManagementResponse) {
                        dailyVisitManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyVisitManagementResponse::class.java
                                )
                                dailyVisitManagementResponse.value = error
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

    fun getDailyVisitsTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getDailyVisitsTeknisi(userId, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyVisitTeknisiResponse>() {
                    override fun onSuccess(t: DailyVisitTeknisiResponse) {
                        dailyVisitTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyVisitTeknisiResponse::class.java
                                )
                                dailyVisitTeknisiResponse.value = error
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

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}