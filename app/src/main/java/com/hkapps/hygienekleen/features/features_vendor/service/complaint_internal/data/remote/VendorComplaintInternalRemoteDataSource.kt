package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.areacomplaint.ComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.areacomplaint.subarea.ComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.chemicalsComplaintInternal.ChemicalsComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.datacomplaintinternal.DataComplaintInternalResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.detailcomplaintinternal.DetailComplaintInternalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.postdatacomplaintinternal.PostDataComplaintInternalResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.processComplaintInternal.ProcessComplaintInternalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.titlecreatecomplaint.TitleCreateComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.typeJobsComplaintInternal.JobsTypeComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.uploadvisitorobject.VisitorObjectResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface VendorComplaintInternalRemoteDataSource {

    fun getComplaintInternalRDS(projectId: String, page: Int) : Single<DataComplaintInternalResponseModel>

    fun getDetailComplaintInternal(
        complaintId: Int
    ): Single<DetailComplaintInternalResponse>

    fun putProcessComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        comments: String,
        idTypeJobs: Int
    ): Single<ProcessComplaintInternalResponse>

    fun putBeforeImageComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse>

    fun putProgressImageComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse>

    fun putAfterImageComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse>

    fun putSubmitComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        reportComments: String,
        totalWorker: Int,
        idChemicals: ArrayList<Int>
    ): Single<ProcessComplaintInternalResponse>

    fun getTitleCreateComplaint(): Single<TitleCreateComplaintResponse>

    fun getAreaComplaint(
        projectId: String
    ): Single<ComplaintAreaResponseModel>

    fun getSubAreaComplaint(
        projectId: String,
        locationId: Int
    ): Single<ComplaintSubAreaResponseModel>

    fun postComplaintInternal(
        userId: Int,
        projectId: String,
        title: Int,
        description: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        image4: MultipartBody.Part,
        idTypeJobs: Int
    ): Single<PostDataComplaintInternalResponseModel>

    fun putCloseComplaintInternal(
        complaintId: Int
    ): Single<DetailComplaintInternalResponse>

    fun getChemicalsComplaint(): Single<ChemicalsComplaintResponse>

    fun getJobsTypeComplaintInternal(): Single<JobsTypeComplaintResponse>

    fun putVisitorObjectComplaint(
        employeeId: Int,
        complaintId: Int,
        idObject: Int,
        file: MultipartBody.Part,
        typeImg: String
    ): Single<VisitorObjectResponseModel>

}