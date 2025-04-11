package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.repository.AuditRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAudit.DetailAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditKualitas.DetailAuditKualitasResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditQuestion.DetailAuditQuestionResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailSubmitPenilaian.DetailSubmitPenilaianResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listFormAudit.ListFormAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listLaporanAudit.ListLaporanAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQualityAudit.ListQualityAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQuestionAudit.ListQuestionAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listWorkResult.ListWorkResultResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.submitFormAudit.SubmitFormAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.ListLaporanAreaResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AuditViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listFormAuditResponse = MutableLiveData<ListFormAuditResponse>()
    val submitFormAuditResponse = MutableLiveData<SubmitFormAuditResponse>()
    val listQualityAuditResponse = MutableLiveData<ListQualityAuditResponse>()
    val submitResultQualityAuditResponse = MutableLiveData<SubmitFormAuditResponse>()
    val listQuestionAuditResponse = MutableLiveData<ListQuestionAuditResponse>()
    val submitPenilaianAuditResponse = MutableLiveData<SubmitFormAuditResponse>()
    val deletePenilaianAuditResponse = MutableLiveData<SubmitFormAuditResponse>()
    val listWorkResultResponse = MutableLiveData<ListWorkResultResponse>()
    val submitHasilKerjaResponse = MutableLiveData<SubmitFormAuditResponse>()
    val listLaporanAuditResponse = MutableLiveData<ListLaporanAuditResponse>()
    val detailAuditResponse = MutableLiveData<DetailAuditResponse>()
    val detailAuditKualitasResponse = MutableLiveData<DetailAuditKualitasResponse>()
    val detailAuditQuestionResponse = MutableLiveData<DetailAuditQuestionResponse>()
    val detailSubmitPenilaianResponse = MutableLiveData<DetailSubmitPenilaianResponse>()
    val updatePenilaianAuditResponse = MutableLiveData<SubmitFormAuditResponse>()
    val listReportHasilKerjaResponse = MutableLiveData<ListLaporanAreaResponse>()
    val updateHasilKerjaResponse = MutableLiveData<SubmitFormAuditResponse>()
    val deleteHasilKerjaResponse = MutableLiveData<SubmitFormAuditResponse>()
    val deleteLaporanAuditResponse = MutableLiveData<SubmitFormAuditResponse>()
    val cekStatusAuditResponse = MutableLiveData<SubmitFormAuditResponse>()

    @Inject
    lateinit var repository: AuditRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListFormAudit(
        createdBy: Int,
        projectCode: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.getListFormAudit(createdBy, projectCode, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListFormAuditResponse>() {
                    override fun onSuccess(t: ListFormAuditResponse) {
                        listFormAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListFormAuditResponse::class.java
                                )
                                listFormAuditResponse.value = error
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

    fun submitFormAudit(
        createdBy: Int,
        projectCode: String,
        periode: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.submitFormAudit(createdBy, projectCode, periode, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        submitFormAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                submitFormAuditResponse.value = error
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

    fun getListQualityAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.getListQualityAudit(userId, projectCode, auditType, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListQualityAuditResponse>() {
                    override fun onSuccess(t: ListQualityAuditResponse) {
                        listQualityAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListQualityAuditResponse::class.java
                                )
                                listQualityAuditResponse.value = error
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

    fun submitResultQualityAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        resultScore: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.submitResultQualityAudit(userId, projectCode, auditType, resultScore, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        submitResultQualityAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                submitResultQualityAuditResponse.value = error
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

    fun getListQuestionAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionType: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.getListQuestionAudit(userId, projectCode, auditType, questionType, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListQuestionAuditResponse>() {
                    override fun onSuccess(t: ListQuestionAuditResponse) {
                        listQuestionAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListQuestionAuditResponse::class.java
                                )
                                listQuestionAuditResponse.value = error
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

    fun submitPenilaianAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionId: Int,
        questionType: String,
        formStatus: String,
        formDescription: String,
        file: MultipartBody.Part,
        date: String
    ) {
        compositeDisposable.add(
            repository.submitPenilaianAudit(userId, projectCode, auditType, questionId, questionType, formStatus, formDescription, file, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        submitPenilaianAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                submitPenilaianAuditResponse.value = error
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

    fun deletePenilaianAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionType: ArrayList<String>,
        date: String
    ) {
        compositeDisposable.add(
            repository.deletePenilaianAudit(userId, projectCode, auditType, questionType, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        deletePenilaianAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                deletePenilaianAuditResponse.value = error
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

    fun getListHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.getListHasilKerja(userId, projectCode, auditType, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListWorkResultResponse>() {
                    override fun onSuccess(t: ListWorkResultResponse) {
                        listWorkResultResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListWorkResultResponse::class.java
                                )
                                listWorkResultResponse.value = error
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

    fun submitHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: Int,
        idObject: Int,
        file: MultipartBody.Part,
        score: Int,
        description: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.submitHasilKerja(userId, projectCode, auditType, idArea, idObject, file, score, description, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        submitHasilKerjaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                submitHasilKerjaResponse.value = error
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

    fun getListLaporanAudit(
        userId: Int,
        projectCode: String,
        periode: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListLaporanAudit(userId, projectCode, periode, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListLaporanAuditResponse>() {
                    override fun onSuccess(t: ListLaporanAuditResponse) {
                        listLaporanAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListLaporanAuditResponse::class.java
                                )
                                listLaporanAuditResponse.value = error
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

    fun getDetailAudit(
        idReport: Int
    ) {
        compositeDisposable.add(
            repository.getDetailAudit(idReport)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAuditResponse>() {
                    override fun onSuccess(t: DetailAuditResponse) {
                        detailAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailAuditResponse::class.java
                                )
                                detailAuditResponse.value = error
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

    fun getDetailAuditKualitas(
        idAuditKualitas: Int
    ) {
        compositeDisposable.add(
            repository.getDetailAuditKualitas(idAuditKualitas)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAuditKualitasResponse>() {
                    override fun onSuccess(t: DetailAuditKualitasResponse) {
                        detailAuditKualitasResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailAuditKualitasResponse::class.java
                                )
                                detailAuditKualitasResponse.value = error
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

    fun getDetailSubmitPenilaian(
        idSubmitQuestion: Int
    ) {
        compositeDisposable.add(
            repository.getDetailSubmitPenilaian(idSubmitQuestion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailSubmitPenilaianResponse>() {
                    override fun onSuccess(t: DetailSubmitPenilaianResponse) {
                        detailSubmitPenilaianResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailSubmitPenilaianResponse::class.java
                                )
                                detailSubmitPenilaianResponse.value = error
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

    fun getDetailAuditQuestion(
        idAuditKualitas: Int,
        questionType: String
    ) {
        compositeDisposable.add(
            repository.getDetailAuditQuestion(idAuditKualitas, questionType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAuditQuestionResponse>() {
                    override fun onSuccess(t: DetailAuditQuestionResponse) {
                        detailAuditQuestionResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailAuditQuestionResponse::class.java
                                )
                                detailAuditQuestionResponse.value = error
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

    fun updatePenilaianAudit(
        idSubmitForm: Int,
        formStatus: String,
        formDescription: String,
        file: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.updatePenilaianAudit(idSubmitForm, formStatus, formDescription, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        updatePenilaianAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                updatePenilaianAuditResponse.value = error
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

    fun getListReportHasilKerja(
        idAuditKualitas: Int,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListReportHasilKerja(idAuditKualitas, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListLaporanAreaResponse>() {
                    override fun onSuccess(t: ListLaporanAreaResponse) {
                        listReportHasilKerjaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListLaporanAreaResponse::class.java
                                )
                                listReportHasilKerjaResponse.value = error
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

    fun updateHasilKerjaAudit(
        idHasilKerja: Int,
        score: Int
    ) {
        compositeDisposable.add(
            repository.updateHasilKerjaAudit(idHasilKerja, score)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        updatePenilaianAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                updateHasilKerjaResponse.value = error
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

    fun deleteHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: ArrayList<Int>,
        date: String
    ) {
        compositeDisposable.add(
            repository.deleteHasilKerja(userId, projectCode, auditType, idArea, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        deleteHasilKerjaResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                deleteHasilKerjaResponse.value = error
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

    fun deleteLaporanAudit(
        userId: Int,
        projectCode: String,
        date: String
    ) {
        compositeDisposable.add(
            repository.deleteLaporanAudit(userId, projectCode, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        deleteLaporanAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                deleteLaporanAuditResponse.value = error
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

    fun getCekStatusAudit(
        userId: Int,
        projectCode: String,
        periode: String
    ) {
        compositeDisposable.add(
            repository.getCekStatusAudit(userId, projectCode, periode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitFormAuditResponse>() {
                    override fun onSuccess(t: SubmitFormAuditResponse) {
                        cekStatusAuditResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitFormAuditResponse::class.java
                                )
                                cekStatusAuditResponse.value = error
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