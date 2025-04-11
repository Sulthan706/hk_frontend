package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.service

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
import retrofit2.http.*

interface VendorComplaintInternalService {
    @GET("/api/v1/complaint/internal/{projectId}")
    fun getComplaintInternalRDS(
        @Path("projectId") projectId: String,
        @Query("page") page: Int,
    ): Single<DataComplaintInternalResponseModel>

    @GET("/api/v1/complaint/{complaintId}")
    fun getDetailComplaintInternal(
        @Path("complaintId") complaintId: Int
    ): Single<DetailComplaintInternalResponse>

    @PUT("/api/v1/complaint/process")
    fun putProcessComplaintInternal(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Query("comments") comments: String,
        @Query("idTypeJobs") idTypeJobs: Int
    ): Single<ProcessComplaintInternalResponse>

    @Multipart
    @PUT("/api/v1/complaint/upload/before")
    fun putBeforeImageComplaintInternal(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse>

    @Multipart
    @PUT("/api/v1/complaint/upload/progress")
    fun putProgressImageComplaintInternal(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse>

    @Multipart
    @PUT("/api/v1/complaint/upload/after")
    fun putAfterImageComplaintInternal(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<ProcessComplaintInternalResponse>

    @PUT("/api/v1/complaint/finish")
    fun putSubmitComplaintInternal(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Query("reportComments") reportComments: String,
        @Query("totalWorker") totalWorker: Int,
        @Query("idChemicals") idChemicals: ArrayList<Int>
    ): Single<ProcessComplaintInternalResponse>

    @GET("/api/v1/complaint/title")
    fun getTitleCreateComplaint(): Single<TitleCreateComplaintResponse>

    @GET("/api/v1/complaint/location/{projectId}")
    fun getAreaComplaint(
        @Path("projectId") projectId: String
    ): Single<ComplaintAreaResponseModel>

    @GET("/api/v1/complaint/sub-location/{projectId}/{locationId}")
    fun getSubAreaComplaint(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int
    ): Single<ComplaintSubAreaResponseModel>

    @Multipart
    @POST("/api/v1/complaint/internal/create")
    fun postComplaintInternal(
        @Query("userId") userId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: Int,
        @Query("description") description: String,
//        @Query("date") date: String,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part,
        @Part file4: MultipartBody.Part,
        @Query("idTypeJobs") idTypeJobs: Int,
    ): Single<PostDataComplaintInternalResponseModel>

    @PUT("/api/v1/complaint/internal/close/{complaintId}")
    fun putCloseComplaintInternal(
        @Path("complaintId") complaintId: Int
    ): Single<DetailComplaintInternalResponse>

    @GET("/api/v1/complaint/chemicals")
    fun getChemicalsComplaint(): Single<ChemicalsComplaintResponse>

    @GET("/api/v1/complaint/type_jobs")
    fun getJobsTypeComplaintInternal(): Single<JobsTypeComplaintResponse>

    @Multipart
    @PUT("/api/v1/complaint/ctalk/visitor/create-progress-v2")
    fun putVisitorObjectComplaint(
        @Query("employeeId") employeeId: Int,
        @Query("complaintId")complaintId: Int,
        @Query("idObject")idObject: Int,
        @Part file: MultipartBody.Part,
        @Query("typeImg") typeImg: String
    ): Single<VisitorObjectResponseModel>

}