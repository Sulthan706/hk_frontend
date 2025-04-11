package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.repository.ProjectRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.BranchesProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.ProjectsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.ProjectsManagementResponse
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

class ProjectViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val branchesProjectResponse = MutableLiveData<BranchesProjectManagementResponse>()
    val projectsManagementResponse = MutableLiveData<ProjectsManagementResponse>()
    val projectsTeknisiResponse = MutableLiveData<ProjectsManagementResponse>()
    val projectsBodResponse = MutableLiveData<ProjectsBodResponse>()

    @Inject
    lateinit var repository: ProjectRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getBranchesProject(
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getBranchesProject(page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BranchesProjectManagementResponse>() {
                    override fun onSuccess(t: BranchesProjectManagementResponse) {
                        branchesProjectResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BranchesProjectManagementResponse::class.java
                                )
                                branchesProjectResponse.value = error
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

    fun getProjectsManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsManagement(userId, keywords, filter, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsManagementResponse>() {
                    override fun onSuccess(t: ProjectsManagementResponse) {
                        projectsManagementResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsManagementResponse::class.java
                                )
                                projectsManagementResponse.value = error
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

    fun getProjectsTeknisi(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsTeknisi(userId, keywords, filter, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsManagementResponse>() {
                    override fun onSuccess(t: ProjectsManagementResponse) {
                        projectsTeknisiResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsManagementResponse::class.java
                                )
                                projectsTeknisiResponse.value = error
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

    fun getProjectBod(
        branchCode: String,
        filter: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectBod(branchCode, filter, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsBodResponse>() {
                    override fun onSuccess(t: ProjectsBodResponse) {
                        projectsBodResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsBodResponse::class.java
                                )
                                projectsBodResponse.value = error
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