package com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.repository.OperationalManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.ErrorGiveRatingEmployeeResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.giveratingemployee.GiveEmployeeRatingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.ratingEmployee.RatingEmployeeResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RatingProfilesViewModel : ViewModel(){
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val getProfileRating = MutableLiveData<RatingEmployeeResponse>()
    private val RatingProfilesViewModel = MutableLiveData<RatingEmployeeResponse>()
    private val giveEmployeeRating = MutableLiveData<GiveEmployeeRatingResponse>()

    private val isErrorgiveEmployeeRating = MutableLiveData<ErrorGiveRatingEmployeeResponse>()


    @Inject
    lateinit var repository: OperationalManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getProfileRating(employeeId: Int){
        compositeDisposable.add(
            repository.getProfileRating(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RatingEmployeeResponse>(){
                    override fun onSuccess(t: RatingEmployeeResponse) {

                        if (t.code == 200){
                            getProfileRating.value = t
                            Log.d("agrii", "$t")
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("agri", "error = $e")
//                        when (e) {
//                            is HttpException -> {
//                                val errorBody = (e as HttpException).response()?.errorBody()
//                                val gson = Gson()
//                                val error = gson.fromJson(
//                                    errorBody?.string(),
//                                    RatingEmployeeResponse::class.java
//                                )
//                                getProfileRating.value = error
//                                isLoading?.value = false
//                            }
//                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
//                                isConnectionTimeout.postValue(true)
//                                isLoading?.value = false
//                            }
//                            else -> isLoading?.value = true
//                        }
                    }
                })
        )
    }

    fun giveRatingEmployee(ratingByUserId: RequestBody,
                           employeeId: RequestBody,
                           projectCode: RequestBody,
                           jobCode: RequestBody,
                           rating: RequestBody
    ){
        compositeDisposable.add(
            repository.giveEmployeeRating(
                ratingByUserId, employeeId, rating, projectCode, jobCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GiveEmployeeRatingResponse>(){
                    override fun onSuccess(t: GiveEmployeeRatingResponse) {

                        if (t.code == 200){
                            giveEmployeeRating.value = t
                            Log.d("give", "response = ${t.status}")
                        }
                    }

                    override fun onError(e: Throwable) {

                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ErrorGiveRatingEmployeeResponse::class.java
                                )
                                isErrorgiveEmployeeRating.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                        Log.e("giveerror", "error= $e")
                    }
                })
        )
    }

    fun giveEmployeeRating(): MutableLiveData<GiveEmployeeRatingResponse> {
        return giveEmployeeRating
    }
    fun getProfileRating(): MutableLiveData<RatingEmployeeResponse> {
        return getProfileRating
    }

    fun isErrorGiveRatingEmployee(): MutableLiveData<ErrorGiveRatingEmployeeResponse>{
        return isErrorgiveEmployeeRating
    }
}