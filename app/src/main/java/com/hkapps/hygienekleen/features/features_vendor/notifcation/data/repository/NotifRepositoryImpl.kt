package com.hkapps.hygienekleen.features.features_vendor.notifcation.data.repository

import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.remote.NotifRemoteDataSource
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
import javax.inject.Inject

class NotifRepositoryImpl @Inject constructor(private val remoteDataSource: NotifRemoteDataSource) :
    NotifRepository {
    override fun getNotif(projectId: String, page: Int): Single<NotifResponseModel> {
        return remoteDataSource.getNotifRDS(projectId, page)
    }

    override fun getNotifMid(
        projectCode: String,
        jobCode: String,
        page: Int
    ): Single<NotifMidResponseModel> {
        return remoteDataSource.getNotifMidRDS(projectCode, jobCode, page)
    }

    override fun getDetailProcessComplaint(complaintId: Int): Single<DetailProcessComplaintResponse> {
        return remoteDataSource.getDetailProcessComplaint(complaintId)
    }

    override fun putProcessComplaint(
        complaintId: Int,
        employeeId: Int,
        comments: String
    ): Single<ProcessComplaintResponseModel> {
        return remoteDataSource.putProcessComplaint(complaintId, employeeId, comments)
    }

    override fun getOperatorComplaint(projectCode: String): Single<OperatorComplaintResponseModel> {
        return remoteDataSource.getOperatorComplaint(projectCode)
    }

    override fun putSubmitOperator(
        complaintId: Int,
        workerId: Int
    ): Single<ProcessComplaintResponseModel> {
        return remoteDataSource.putSubmitOperator(complaintId, workerId)
    }

    override fun putBeforeImageComplaint(
        complaintId: Int,
        employeeId: Int,
        beforeImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel> {
        return remoteDataSource.putBeforeImageComplaint(complaintId, employeeId, beforeImage)
    }

    override fun putProgressImageComplaint(
        complaintId: Int,
        employeeId: Int,
        progressImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel> {
        return remoteDataSource.putProgressImageComplaint(complaintId, employeeId, progressImage)
    }

    override fun putAfterImageComplaint(
        complaintId: Int,
        employeeId: Int,
        afterImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel> {
        return remoteDataSource.putAfterImageComplaint(complaintId, employeeId, afterImage)
    }

    override fun putSubmitComplaint(
        complaintId: Int,
        employeeId: Int
    ): Single<ProcessComplaintResponseModel> {
        return remoteDataSource.putSubmitComplaint(complaintId, employeeId)
    }
    //notif mid
    override fun getNotifHistoryLeader(
        employeeId: Int,
        projectCode: String,
        category: String
    ): Single<NotificationMidHistory> {
        return remoteDataSource.getNotifHistoryLeader(employeeId, projectCode, category)
    }

    override fun getNotifHistoryOperator(
        employeeId: Int,
        projectCode: String,
        category: String
    ): Single<NotificationMidHistory> {
        return remoteDataSource.getNotifHistoryOperator(employeeId, projectCode, category)
    }

    override fun getNotificationHistoryLeader(
        employeeId: Int,
        projectCode: String,
        category: String,
        page: Int
    ): Single<NotificationMidResponseModel> {
        return remoteDataSource.getNotificationHistoryLeader(employeeId, projectCode, category, page)
    }

    override fun getNotificationHistoryOperator(
        employeeId: Int,
        projectCode: String,
        category: String,
        page: Int
    ): Single<NotificationMidResponseModel> {
        return remoteDataSource.getNotificationHistoryOperator(employeeId, projectCode, category, page)
    }

    override fun putReadNotificationMid(
        notificationId: Int,
        employeeId: Int
    ): Single<ReadNotificationMidResponseModel> {
        return remoteDataSource.putReadNotificationMid(employeeId, notificationId)
    }
}