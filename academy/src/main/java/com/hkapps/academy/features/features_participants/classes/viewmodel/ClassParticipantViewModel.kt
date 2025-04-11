package com.hkapps.academy.features.features_participants.classes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.academy.di.DaggerAcademyOperationComponent
import com.hkapps.academy.features.features_participants.classes.data.repository.ClassParticipantRepository
import com.hkapps.academy.features.features_participants.classes.model.listClass.ClassesParticipantResponse
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

class ClassParticipantViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    var classesParticipantModel = MutableLiveData<ClassesParticipantResponse>()

    @Inject
    lateinit var repository: ClassParticipantRepository

    init {
        DaggerAcademyOperationComponent.create().injectRepository(this)
    }

    fun getListClassParticipant(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getListClassParticipant(userNuc, projectCode, levelJabatan, date, region, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ClassesParticipantResponse>() {
                    override fun onSuccess(t: ClassesParticipantResponse) {
                        classesParticipantModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ClassesParticipantResponse::class.java
                                )
                                classesParticipantModel.value = error
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
        super.onCleared()
        compositeDisposable.dispose()
    }
}