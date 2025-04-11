package com.hkapps.hygienekleen.features.features_management.project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.project.data.repository.ProjectManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listClient.ListClientProjectMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listbranch.ListAllBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject.ListProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.ListProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.attendanceProject.AttendanceProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.complaintProject.ComplaintProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.detailProject.DetailProjectManagementResponse
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

class ProjectManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    private val listProjectModel = MutableLiveData<ListProjectManagementResponseModel>()
    private val projectModel = MutableLiveData<ListProjectResponseModel>()
    private val listBranchModel = MutableLiveData<ListAllBranchResponseModel>()
    private val searchProjectModel = MutableLiveData<ListProjectManagementResponseModel>()
    val detailProjectModel = MutableLiveData<DetailProjectManagementResponse>()
    val complaintProjectModel = MutableLiveData<ComplaintProjectManagementResponse>()
    val attendanceProjectModel = MutableLiveData<AttendanceProjectManagementResponse>()
    val listClientProjectModel = MutableLiveData<ListClientProjectMgmntResponse>()
    //cftalk
    val complaintInternalModel = MutableLiveData<ComplaintProjectManagementResponse>()

    @Inject
    lateinit var repository: ProjectManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListProject(branchCode: String, page: Int, perPage: Int){
        compositeDisposable.add(
            repository.getListProjectManagement(branchCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectManagementResponseModel>() {
                    override fun onSuccess(t: ListProjectManagementResponseModel) {
                        if(t.code == 200){
                            listProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectManagementResponseModel::class.java
                                )
                                listProjectModel.value = error
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

    fun getProject(id: Int) {
        compositeDisposable.add(
            repository.getListProject(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ListProjectResponseModel>() {
                override fun onSuccess(t: ListProjectResponseModel) {
                    if (t.code == 200) {
                        projectModel.value = t
                    }
                    isLoading?.value = false
                }

                override fun onError(e: Throwable) {
                    when (e) {
                        is HttpException -> {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListProjectResponseModel::class.java
                            )
                            projectModel.value = error
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

    fun getListBranch(){
        compositeDisposable.add(
            repository.getListBranch()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAllBranchResponseModel>(){
                    override fun onSuccess(t: ListAllBranchResponseModel) {
                        if(t.code == 200){
                            listBranchModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAllBranchResponseModel::class.java
                                )
                                listBranchModel.value = error
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

    fun getSearchProjectAll(page: Int, branchCode: String, keywords: String, perPage: Int){
        compositeDisposable.add(
            repository.getSearchProjectAll(page, branchCode, keywords, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectManagementResponseModel>() {
                    override fun onSuccess(t: ListProjectManagementResponseModel) {
                        if(t.code == 200){
                            searchProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectManagementResponseModel::class.java
                                )
                                searchProjectModel.value = error
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

    fun getDetailProject(projectCode: String) {
        compositeDisposable.add(
            repository.getDetailProject(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailProjectManagementResponse>() {
                    override fun onSuccess(t: DetailProjectManagementResponse) {
                        if (t.code == 200) {
                            detailProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailProjectManagementResponse::class.java
                                )
                                detailProjectModel.value = error
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

    fun getComplaintProject(userId: Int, projectId: String, complaintType: ArrayList<String>) {
        compositeDisposable.add(
            repository.getComplaintProject(userId, projectId, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ComplaintProjectManagementResponse>() {
                    override fun onSuccess(t: ComplaintProjectManagementResponse) {
                        if (t.code == 200) {
                            complaintProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ComplaintProjectManagementResponse::class.java
                                )
                                complaintProjectModel.value = error
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
    fun getComplaintInternalProject(userId: Int, projectId: String, complaintType: ArrayList<String>) {
        compositeDisposable.add(
            repository.getComplaintProject(userId, projectId, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ComplaintProjectManagementResponse>() {
                    override fun onSuccess(t: ComplaintProjectManagementResponse) {
                        if (t.code == 200) {
                            complaintInternalModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ComplaintProjectManagementResponse::class.java
                                )
                                complaintInternalModel.value = error
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


    fun getAttendanceProject(projectCode: String, month: Int, year: Int) {
        compositeDisposable.add(
            repository.getAttendanceProject(projectCode, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceProjectManagementResponse>() {
                    override fun onSuccess(t: AttendanceProjectManagementResponse) {
                        if (t.code == 200) {
                            attendanceProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceProjectManagementResponse::class.java
                                )
                                attendanceProjectModel.value = error
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

    fun getListClientProject(projectCode: String) {
        compositeDisposable.add(
            repository.getListClientProject(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListClientProjectMgmntResponse>() {
                    override fun onSuccess(t: ListClientProjectMgmntResponse) {
                        if (t.code == 200) {
                            listClientProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListClientProjectMgmntResponse::class.java
                                )
                                listClientProjectModel.value = error
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

    fun getComplaintInternalViewModel(): MutableLiveData<ComplaintProjectManagementResponse>{
        return complaintInternalModel
    }

    fun getListBranchProjectManagement(): MutableLiveData<ListProjectManagementResponseModel> {
        return listProjectModel
    }

    fun getProjectModel(): MutableLiveData<ListProjectResponseModel>{
        return projectModel
    }

    fun getListBranchResponse(): MutableLiveData<ListAllBranchResponseModel>{
        return listBranchModel
    }

    fun getSearchProjectAllResponse(): MutableLiveData<ListProjectManagementResponseModel>{
        return searchProjectModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}