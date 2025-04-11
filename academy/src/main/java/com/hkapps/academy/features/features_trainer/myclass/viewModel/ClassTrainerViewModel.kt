package com.hkapps.academy.features.features_trainer.myclass.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.academy.di.DaggerAcademyOperationComponent
import com.hkapps.academy.features.features_trainer.myclass.data.repository.ClassTrainerRepository
import com.hkapps.academy.features.features_trainer.myclass.model.createClass.CreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.detailTraining.DetailTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.inviteParticipant.InviteParticipantTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listModule.ModulesCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ParticipantsClassTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listProject.ProjectsCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.ParticipantsTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.putTrainingDetailTrainer.UpdateTrainingTrainerResponse
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

class ClassTrainerViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listParticipantInviteModel = MutableLiveData<ParticipantsClassTrainerResponse>()
    val searchParticipantNameModel = MutableLiveData<ParticipantsClassTrainerResponse>()
    val listModuleCreateClassModel = MutableLiveData<ModulesCreateClassResponse>()
    val listProjectCreateClassModel = MutableLiveData<ProjectsCreateClassResponse>()
    val locationProjectCreateClassModel = MutableLiveData<ProjectsCreateClassResponse>()
    val createClassResponseModel = MutableLiveData<CreateClassResponse>()
    val projectsInviteModel = MutableLiveData<ProjectsCreateClassResponse>()
    val inviteParticipantModel = MutableLiveData<InviteParticipantTrainerResponse>()
    val detailTrainingModel = MutableLiveData<DetailTrainingTrainerResponse>()
    val participantsTrainingModel = MutableLiveData<ParticipantsTrainingTrainerResponse>()
    val updateAttendanceParticipantModel = MutableLiveData<UpdateTrainingTrainerResponse>()
    val updateEndClassTrainingModel = MutableLiveData<UpdateTrainingTrainerResponse>()
    val updateScoreTrainingModel = MutableLiveData<UpdateTrainingTrainerResponse>()

    @Inject
    lateinit var repository: ClassTrainerRepository

    init {
        DaggerAcademyOperationComponent.create().injectRepository(this)
    }

    fun getListParticipantClass(
        name: String,
        projectCode: String,
        position: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListParticipantClass(name, projectCode, position, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ParticipantsClassTrainerResponse>() {
                    override fun onSuccess(t: ParticipantsClassTrainerResponse) {
                        listParticipantInviteModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ParticipantsClassTrainerResponse::class.java
                                )
                                listParticipantInviteModel.value = error
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

    fun getSearchParticipantName(
        name: String,
        projectCode: String,
        position: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListParticipantClass(name, projectCode, position, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ParticipantsClassTrainerResponse>() {
                    override fun onSuccess(t: ParticipantsClassTrainerResponse) {
                        searchParticipantNameModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ParticipantsClassTrainerResponse::class.java
                                )
                                searchParticipantNameModel.value = error
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

    fun getListModuleCreateClass() {
        compositeDisposable.add(
            repository.getListModuleCreateClass()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ModulesCreateClassResponse>() {
                    override fun onSuccess(t: ModulesCreateClassResponse) {
                        listModuleCreateClassModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ModulesCreateClassResponse::class.java
                                )
                                listModuleCreateClassModel.value = error
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

    fun getListProjectCreateClass(
        projectName: String
    ) {
        compositeDisposable.add(
            repository.getListProjectCreateClass(projectName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsCreateClassResponse>() {
                    override fun onSuccess(t: ProjectsCreateClassResponse) {
                        listProjectCreateClassModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsCreateClassResponse::class.java
                                )
                                listProjectCreateClassModel.value = error
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

    fun getLocationProjectCreateClass(
        projectName: String
    ) {
        compositeDisposable.add(
            repository.getListProjectCreateClass(projectName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsCreateClassResponse>() {
                    override fun onSuccess(t: ProjectsCreateClassResponse) {
                        locationProjectCreateClassModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsCreateClassResponse::class.java
                                )
                                locationProjectCreateClassModel.value = error
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

    fun createClassTrainer(
        trainerNuc: String,
        trainingName: String,
        participant: String,
        moduleId: Int,
        assignOnlineTest: String,
        category: String,
        quota: Int,
        projectCode: String,
        trainingDate: String,
        trainingStart: String,
        trainingEnd: String,
        region: String,
        jenisKelas: String,
        trainingLocationCode: String,
        locationDescription: String,
        alternateLocation: String,
        appName: String,
        appLink: String
    ) {
        compositeDisposable.add(
            repository.createClassTrainer(trainerNuc, trainingName, participant, moduleId, assignOnlineTest, category, quota, projectCode, trainingDate, trainingStart, trainingEnd, region, jenisKelas, trainingLocationCode, locationDescription, alternateLocation, appName, appLink)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateClassResponse>() {
                    override fun onSuccess(t: CreateClassResponse) {
                        createClassResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateClassResponse::class.java
                                )
                                createClassResponseModel.value = error
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

    fun getProjectsInviteParticipant(
        projectName: String
    ) {
        compositeDisposable.add(
            repository.getListProjectCreateClass(projectName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsCreateClassResponse>() {
                    override fun onSuccess(t: ProjectsCreateClassResponse) {
                        projectsInviteModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsCreateClassResponse::class.java
                                )
                                projectsInviteModel.value = error
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

    fun inviteParticipant(
        userNuc: ArrayList<String>,
        trainingId: Int
    ) {
        compositeDisposable.add(
            repository.inviteParticipant(userNuc, trainingId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<InviteParticipantTrainerResponse>(){
                    override fun onSuccess(t: InviteParticipantTrainerResponse) {
                        inviteParticipantModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    InviteParticipantTrainerResponse::class.java
                                )
                                inviteParticipantModel.value = error
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

    fun getDetailTraining(
        trainingId: Int,
        region: String
    ) {
        compositeDisposable.add(
            repository.getDetailTraining(trainingId, region)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailTrainingTrainerResponse>(){
                    override fun onSuccess(t: DetailTrainingTrainerResponse) {
                        detailTrainingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailTrainingTrainerResponse::class.java
                                )
                                detailTrainingModel.value = error
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

    fun getParticipantsTraining(
        trainingId: Int,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getParticipantsTraining(trainingId, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ParticipantsTrainingTrainerResponse>() {
                    override fun onSuccess(t: ParticipantsTrainingTrainerResponse) {
                        participantsTrainingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ParticipantsTrainingTrainerResponse::class.java
                                )
                                participantsTrainingModel.value = error
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

    fun updateAttendanceParticipant(
        participantId: Int,
        statusAttendance: String,
        userNuc: String
    ) {
        compositeDisposable.add(
            repository.updateAttendanceParticipant(participantId, statusAttendance, userNuc)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateTrainingTrainerResponse>() {
                    override fun onSuccess(t: UpdateTrainingTrainerResponse) {
                        updateAttendanceParticipantModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateTrainingTrainerResponse::class.java
                                )
                                updateAttendanceParticipantModel.value = error
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

    fun updateEndClassTraining(
        trainingId: Int,
        userNuc: String
    ) {
        compositeDisposable.add(
            repository.updateEndClassTraining(trainingId, userNuc)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateTrainingTrainerResponse>() {
                    override fun onSuccess(t: UpdateTrainingTrainerResponse) {
                        updateEndClassTrainingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateTrainingTrainerResponse::class.java
                                )
                                updateEndClassTrainingModel.value = error
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

    fun updateScoreTraining(
        participantId: Int,
        nilai: Int,
        komentar: String,
        userNuc: String
    ) {
        compositeDisposable.add(
            repository.updateScoreTraining(participantId, nilai, komentar, userNuc)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateTrainingTrainerResponse>() {
                    override fun onSuccess(t: UpdateTrainingTrainerResponse) {
                        updateScoreTrainingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateTrainingTrainerResponse::class.java
                                )
                                updateScoreTrainingModel.value = error
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