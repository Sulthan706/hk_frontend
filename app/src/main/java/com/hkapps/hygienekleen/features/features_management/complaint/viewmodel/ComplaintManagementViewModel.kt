package com.hkapps.hygienekleen.features.features_management.complaint.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.complaint.data.repository.ComplaintManagementRepository
import com.hkapps.hygienekleen.features.features_management.complaint.model.createComplaint.CreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.dashboardcomplaintmanagement.DashboardManagementComplaintResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.detailComplaint.DetailComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listAreaComplaint.AreaCreatComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listBranch.ListBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.listComplaint.ListComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProject.ListProjectFmGmResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll.ListProjectAllResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listSubAreaComplaint.SubAreaCreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listTitleComplaint.TitleCreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listcomplaintctalk.ListCtalkManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.projectCount.ProjectCountCtalkManageResponse
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

class ComplaintManagementViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val isNull = MutableLiveData<Boolean>()

    private val listProjectModel = MutableLiveData<ListProjectFmGmResponse>()
    private val countProjectModel = MutableLiveData<ProjectCountCtalkManageResponse>()
    private val listComplaintModel = MutableLiveData<ListComplaintManagementResponse>()
    private val detailComplaintModel = MutableLiveData<DetailComplaintManagementResponse>()
    private val listProjectAllModel = MutableLiveData<ListProjectAllResponse>()
    private val listBranchModel = MutableLiveData<ListBranchResponseModel>()
    private val listProjectByBranchModel = MutableLiveData<ListProjectAllResponse>()
    private val searchProjectModel = MutableLiveData<ListProjectAllResponse>()
    val createComplaintManagementModel = MutableLiveData<CreateComplaintManagementResponse>()
    val titleCreateComplaintModel = MutableLiveData<TitleCreateComplaintManagementResponse>()
    val areaCreateComplaintManagementModel = MutableLiveData<AreaCreatComplaintManagementResponse>()
    val subAreaCreateComplaintManagementModel = MutableLiveData<SubAreaCreateComplaintManagementResponse>()
    private val dashboardComplaintManagementModel = MutableLiveData<DashboardManagementComplaintResponseModel>()
    private val listCtalkManagementModel = MutableLiveData<ListCtalkManagementResponseModel>()


    @Inject
    lateinit var repository: ComplaintManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListProjectFmGm(employeeId: Int) {
        compositeDisposable.add(
            repository.getListProjectFmGM(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectFmGmResponse>() {
                    override fun onSuccess(t: ListProjectFmGmResponse) {
                        if (t.code == 200) {
                            listProjectModel.value = t
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
                                    ListProjectFmGmResponse::class.java
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

    fun getCountProject(userId: Int, projectId: String) {
        compositeDisposable.add(
            repository.getProjectCountCtalkManage(userId, projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectCountCtalkManageResponse>() {
                    override fun onSuccess(t: ProjectCountCtalkManageResponse) {
                        if (t.code == 200) {
                            countProjectModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectCountCtalkManageResponse::class.java
                                )
                                countProjectModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getListComplaintManagement(page: Int, projectCode: String, complaintType: String) {
        compositeDisposable.add(
            repository.getComplaintManagement(page, projectCode, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListComplaintManagementResponse>() {
                    override fun onSuccess(t: ListComplaintManagementResponse) {
                        if (t.code == 200) {
                            listComplaintModel.value = t
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
                                    ListComplaintManagementResponse::class.java
                                )
                                listComplaintModel.value = error
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

    fun getDetailComplaintManagement(complaintId: Int) {
        compositeDisposable.add(
            repository.getDetailComplaintManagement(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailComplaintManagementResponse>() {
                    override fun onSuccess(t: DetailComplaintManagementResponse) {
                        if (t.code == 200) {
                            detailComplaintModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailComplaintManagementResponse::class.java
                                )
                                detailComplaintModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getListProjectAll(page: Int, size: Int) {
        compositeDisposable.add(
            repository.getAllListProjectManagement(page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectAllResponse>() {
                    override fun onSuccess(t: ListProjectAllResponse) {
                        if (t.code == 200) {
                            listProjectAllModel.value = t
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
                                    ListProjectAllResponse::class.java
                                )
                                listProjectAllModel.value = error
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
                .subscribeWith(object : DisposableSingleObserver<ListBranchResponseModel>() {
                    override fun onSuccess(t: ListBranchResponseModel) {
                        if (t.code == 200) {
                            listBranchModel.value = t
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
                                    ListBranchResponseModel::class.java
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

    fun getListProjectByBranch(branchCode: String, page: Int, perPage: Int) {
        compositeDisposable.add(
            repository.getListProjectByBranch(branchCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectAllResponse>() {
                    override fun onSuccess(t: ListProjectAllResponse) {
                        if (t.code == 200) {
                            listProjectByBranchModel.value = t
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
                                    ListProjectAllResponse::class.java
                                )
                                listProjectByBranchModel.value = error
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

    fun getSearchProjectAll(page: Int, branchCode: String, keywords: String, perPage: Int) {
        compositeDisposable.add(
            repository.getSearchProjectAll(page, branchCode, keywords, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectAllResponse>() {
                    override fun onSuccess(t: ListProjectAllResponse) {
                        if (t.code == 200) {
                            searchProjectModel.value = t
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
                                    ListProjectAllResponse::class.java
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

    fun postComplaintManagement(
        userId: Int,
        projectId: String,
        title: Int,
        description: String,
        locationId: Int,
        subLocationId: Int,
        file: MultipartBody.Part,
        file2: MultipartBody.Part,
        file3: MultipartBody.Part,
        file4: MultipartBody.Part,
        complaintType: String
    ) {
        compositeDisposable.add(
            repository.postComplaintManagement(userId, projectId, title, description, locationId, subLocationId, file, file2, file3, file4, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateComplaintManagementResponse>() {
                    override fun onSuccess(t: CreateComplaintManagementResponse) {
                        if (t.code == 200) {
                            createComplaintManagementModel.value = t
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
                                    CreateComplaintManagementResponse::class.java
                                )
                                createComplaintManagementModel.value = error
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
    fun getListTitleComplaint() {
        compositeDisposable.add(
            repository.getTitleCreateComplaint()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TitleCreateComplaintManagementResponse>() {
                    override fun onSuccess(t: TitleCreateComplaintManagementResponse) {
                        if (t.code == 200) {
                            titleCreateComplaintModel.value = t
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
                                    TitleCreateComplaintManagementResponse::class.java
                                )
                                titleCreateComplaintModel.value = error
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

    fun getListAreaComplaint(projectId: String) {
        compositeDisposable.add(
            repository.getAreaCreateComplaint(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AreaCreatComplaintManagementResponse>() {
                    override fun onSuccess(t: AreaCreatComplaintManagementResponse) {
                        if (t.code == 200) {
                            areaCreateComplaintManagementModel.value = t
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
                                    AreaCreatComplaintManagementResponse::class.java
                                )
                                areaCreateComplaintManagementModel.value = error
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

    fun getListSubAreaComplaint(projectId: String, locationId: Int) {
        compositeDisposable.add(
            repository.getSubAreaCreateComplaint(projectId, locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubAreaCreateComplaintManagementResponse>() {
                    override fun onSuccess(t: SubAreaCreateComplaintManagementResponse) {
                        if (t.code == 200) {
                            subAreaCreateComplaintManagementModel.value = t
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
                                    SubAreaCreateComplaintManagementResponse::class.java
                                )
                                subAreaCreateComplaintManagementModel.value = error
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

    fun getListCtalkManagement(page: Int, projectCode: String, statusComplaint: String){
        compositeDisposable.add(
            repository.getCtalkManagement(page, projectCode, statusComplaint)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCtalkManagementResponseModel>(){
                    override fun onSuccess(t: ListCtalkManagementResponseModel) {
                        listCtalkManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCtalkManagementResponseModel::class.java
                                )
                                listCtalkManagementModel.value = error
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

    fun getDashboardComplaintManagement(projectId: String){
        compositeDisposable.add(
            repository.getDashboardComplaintManagement(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DashboardManagementComplaintResponseModel>(){
                    override fun onSuccess(t: DashboardManagementComplaintResponseModel) {
                        dashboardComplaintManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DashboardManagementComplaintResponseModel::class.java
                                )
                                dashboardComplaintManagementModel.value = error
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

    fun getDashboardComplaintManagementViewModel(): MutableLiveData<DashboardManagementComplaintResponseModel>{
        return dashboardComplaintManagementModel
    }

    fun getListCtalkViewModel(): MutableLiveData<ListCtalkManagementResponseModel>{
        return listCtalkManagementModel
    }

    fun getListProjectFmGmResponse(): MutableLiveData<ListProjectFmGmResponse> {
        return listProjectModel
    }

    fun getProjectCountResponse(): MutableLiveData<ProjectCountCtalkManageResponse> {
        return countProjectModel
    }

    fun getListComplaintManagementResponse(): MutableLiveData<ListComplaintManagementResponse> {
        return listComplaintModel
    }

    fun getDetailComplaintManagementResponse(): MutableLiveData<DetailComplaintManagementResponse> {
        return detailComplaintModel
    }

    fun getListProjectAllResponse(): MutableLiveData<ListProjectAllResponse> {
        return listProjectAllModel
    }

    fun getListBranchResponse(): MutableLiveData<ListBranchResponseModel> {
        return listBranchModel
    }

    fun getListProjectByBranchResponse(): MutableLiveData<ListProjectAllResponse> {
        return listProjectByBranchModel
    }

    fun getSearchProjectAllResponse(): MutableLiveData<ListProjectAllResponse> {
        return searchProjectModel
    }

    fun checkNull(desc: String) {
        isNull.value = desc.isEmpty()
    }

    fun getDesc(): MutableLiveData<Boolean> {
        return isNull
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}