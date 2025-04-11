package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.remote


import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.service.VendorComplaintInternalService
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
import javax.inject.Inject

class VendorComplaintInternalRemoteDataSourceImpl @Inject constructor(private val service: VendorComplaintInternalService) : VendorComplaintInternalRemoteDataSource {
    override fun getComplaintInternalRDS(
        projectId: String,
        page: Int
    ): Single<DataComplaintInternalResponseModel> {
        return service.getComplaintInternalRDS(projectId, page)
    }

    override fun getDetailComplaintInternal(complaintId: Int): Single<DetailComplaintInternalResponse> {
        return service.getDetailComplaintInternal(complaintId)
    }

    override fun putProcessComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        comments: String,
        idTypeJobs: Int
    ): Single<ProcessComplaintInternalResponse> {
        return service.putProcessComplaintInternal(complaintId, employeeId, comments, idTypeJobs)
    }

    override fun putBeforeImageComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse> {
        return service.putBeforeImageComplaintInternal(complaintId, employeeId, file)
    }

    override fun putProgressImageComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse> {
        return service.putProgressImageComplaintInternal(complaintId, employeeId, file)
    }

    override fun putAfterImageComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse> {
        return service.putAfterImageComplaintInternal(complaintId, employeeId, file)
    }

    override fun putSubmitComplaintInternal(
        complaintId: Int,
        employeeId: Int,
        reportComments: String,
        totalWorker: Int,
        idChemicals: ArrayList<Int>
    ): Single<ProcessComplaintInternalResponse> {
        return service.putSubmitComplaintInternal(complaintId, employeeId, reportComments, totalWorker, idChemicals)
    }

    override fun getTitleCreateComplaint(): Single<TitleCreateComplaintResponse> {
        return service.getTitleCreateComplaint()
    }

    override fun getAreaComplaint(projectId: String): Single<ComplaintAreaResponseModel> {
        return service.getAreaComplaint(projectId)
    }

    override fun getSubAreaComplaint(
        projectId: String,
        locationId: Int
    ): Single<ComplaintSubAreaResponseModel> {
        return service.getSubAreaComplaint(projectId, locationId)
    }

    override fun postComplaintInternal(
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
    ): Single<PostDataComplaintInternalResponseModel> {
        return service.postComplaintInternal(userId, projectId, title, description, locationId, subLocationId, image, image2, image3, image4, idTypeJobs)
    }

    override fun putCloseComplaintInternal(complaintId: Int): Single<DetailComplaintInternalResponse> {
        return service.putCloseComplaintInternal(complaintId)
    }

    override fun getChemicalsComplaint(): Single<ChemicalsComplaintResponse> {
        return service.getChemicalsComplaint()
    }

    override fun getJobsTypeComplaintInternal(): Single<JobsTypeComplaintResponse> {
        return service.getJobsTypeComplaintInternal()
    }

    override fun putVisitorObjectComplaint(
        employeeId: Int,
        complaintId: Int,
        idObject: Int,
        file: MultipartBody.Part,
        typeImg: String
    ): Single<VisitorObjectResponseModel> {
        return service.putVisitorObjectComplaint(employeeId, complaintId, idObject, file, typeImg)
    }

}