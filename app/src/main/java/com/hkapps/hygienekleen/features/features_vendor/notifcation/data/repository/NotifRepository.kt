package com.hkapps.hygienekleen.features.features_vendor.notifcation.data.repository

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

interface NotifRepository {
    fun getNotif(
        projectId: String,
        page: Int
    ): Single<NotifResponseModel>

    fun getNotifMid(
        projectCode: String,
        jobCode: String,
        page: Int
    ): Single<NotifMidResponseModel>

    fun getDetailProcessComplaint(
        complaintId: Int
    ): Single<DetailProcessComplaintResponse>

    fun putProcessComplaint(
        complaintId: Int,
        employeeId: Int,
        comments: String
    ): Single<ProcessComplaintResponseModel>

    fun getOperatorComplaint(
        projectCode: String
    ): Single<OperatorComplaintResponseModel>

    fun putSubmitOperator(
        complaintId: Int,
        workerId: Int
    ): Single<ProcessComplaintResponseModel>

    fun putBeforeImageComplaint(
        complaintId: Int,
        employeeId: Int,
        beforeImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel>

    fun putProgressImageComplaint(
        complaintId: Int,
        employeeId: Int,
        progressImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel>

    fun putAfterImageComplaint(
        complaintId: Int,
        employeeId: Int,
        afterImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel>

    fun putSubmitComplaint(
        complaintId: Int,
        employeeId: Int
    ): Single<ProcessComplaintResponseModel>

    fun getNotifHistoryLeader(
        employeeId: Int,
        projectCode: String,
        category: String
    ): Single<NotificationMidHistory>

    fun getNotifHistoryOperator(
        employeeId: Int,
        projectCode: String,
        category: String
    ): Single<NotificationMidHistory>
    // new
    fun getNotificationHistoryLeader(
        employeeId: Int,
        projectCode: String,
        category: String,
        page: Int
    ): Single<NotificationMidResponseModel>

    fun getNotificationHistoryOperator(
        employeeId: Int,
        projectCode: String,
        category: String,
        page: Int
    ): Single<NotificationMidResponseModel>


    fun putReadNotificationMid(
        notificationId: Int,
        employeeId: Int
    ): Single<ReadNotificationMidResponseModel>
}