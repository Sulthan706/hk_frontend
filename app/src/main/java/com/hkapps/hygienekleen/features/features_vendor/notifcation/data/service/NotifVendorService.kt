package com.hkapps.hygienekleen.features.features_vendor.notifcation.data.service

import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.NotifResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.detailProcessComplaint.DetailProcessComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.listOperator.OperatorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel.NotifMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.NotificationMidHistory
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.NotificationMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.readnotifmid.ReadNotificationMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.processComplaint.ProcessComplaintResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface NotifVendorService {

    @GET("/api/v1/complaint/project")
    fun getNotifComplaintApi(
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int?
    ): Single<NotifResponseModel>

    //    @GET("/api/v1/complaint/project/escalation")
    @GET("/api/v1/complaint/project")
    fun getNotifMidComplaintApi(
        @Query("projectCode") projectCode: String,
        @Query("jobCode") jobCode: String,
        @Query("page") page: Int?
    ): Single<NotifMidResponseModel>

    @GET("/api/v1/complaint/{complaintId}")
    fun getDetailProcessComplaint(
        @Path("complaintId") complaintId: Int
    ): Single<DetailProcessComplaintResponse>

    @PUT("/api/v1/complaint/process")
    fun putProcessComplaint(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Query("comments") comments: String
    ): Single<ProcessComplaintResponseModel>

    @GET("/api/v1/complaint/operator/cso/{projectCode}")
    fun getOperatorComplaint(
        @Path("projectCode") projectCode: String
    ): Single<OperatorComplaintResponseModel>

    @PUT("/api/v1/complaint/operator/cso/submit")
    fun putSubmitOperator(
        @Query("complaintId") complaintId: Int,
        @Query("workerId") workerId: Int
    ): Single<ProcessComplaintResponseModel>

    @Multipart
    @PUT("/api/v1/complaint/upload/before")
    fun putBeforeImageComplaint(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel>

    @Multipart
    @PUT("/api/v1/complaint/upload/progress")
    fun putProgressImageComplaint(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel>

    @Multipart
    @PUT("/api/v1/complaint/upload/after")
    fun putAfterImageComplaint(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel>

    @PUT("/api/v1/complaint/finish")
    fun putSubmitComplaint(
        @Query("complaintId") complaintId: Int,
        @Query("employeeId") employeeId: Int
    ): Single<ProcessComplaintResponseModel>
    //get notif mid history
    //old
        @GET("/api/v1/notification/history/leader")
        fun getNotifHistoryLeader(
            @Query("employeeId") employeeId: Int,
            @Query("projectCode") projectCode: String,
            @Query("category") category: String
        ): Single<NotificationMidHistory>

        @GET("/api/v1/notification/history/operator")
        fun getNotifHistoryOperator(
            @Query("employeeId") employeeId: Int,
            @Query("projectCode") projectCode: String,
            @Query("category") category: String
        ): Single<NotificationMidHistory>
        //new
        @GET("/api/v1/notification/history/leader")
        fun getNotificationHistoryLeader(
            @Query("employeeId") employeeId: Int,
            @Query("projectCode") projectCode: String,
            @Query("category") category: String,
            @Query("page") page: Int
        ): Single<NotificationMidResponseModel>

        @GET("/api/v1/notification/history/operator")
        fun getNotificationHistoryOperator(
            @Query("employeeId") employeeId: Int,
            @Query("projectCode") projectCode: String,
            @Query("category") category: String,
            @Query("page") page: Int
        ): Single<NotificationMidResponseModel>

        @PUT("/api/v1/notification/history/read")
        fun putReadNotificationMid(
            @Query("notificationId") notificationId: Int,
            @Query("employeeId") employeeId: Int,
        ): Single<ReadNotificationMidResponseModel>
}