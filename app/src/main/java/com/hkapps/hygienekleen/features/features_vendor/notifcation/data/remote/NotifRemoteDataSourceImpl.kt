package com.hkapps.hygienekleen.features.features_vendor.notifcation.data.remote

import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.service.NotifVendorService
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

class NotifRemoteDataSourceImpl @Inject constructor(private val vendorService: NotifVendorService) :
    NotifRemoteDataSource {

    override fun getNotifRDS(projectId: String, page: Int): Single<NotifResponseModel> {
        return vendorService.getNotifComplaintApi(projectId, page)
    }

    override fun getNotifMidRDS(projectId: String, jobCode: String ,page: Int): Single<NotifMidResponseModel> {
        return vendorService.getNotifMidComplaintApi(projectId, jobCode, page)
    }

    override fun getDetailProcessComplaint(complaintId: Int): Single<DetailProcessComplaintResponse> {
        return vendorService.getDetailProcessComplaint(complaintId)
    }

    override fun putProcessComplaint(
        complaintId: Int,
        employeeId: Int,
        comments: String
    ): Single<ProcessComplaintResponseModel> {
        return vendorService.putProcessComplaint(complaintId, employeeId, comments)
    }

    override fun getOperatorComplaint(projectCode: String): Single<OperatorComplaintResponseModel> {
        return vendorService.getOperatorComplaint(projectCode)
    }

    override fun putSubmitOperator(
        complaintId: Int,
        workerId: Int
    ): Single<ProcessComplaintResponseModel> {
        return vendorService.putSubmitOperator(complaintId, workerId)
    }

    override fun putBeforeImageComplaint(
        complaintId: Int,
        employeeId: Int,
        beforeImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel> {
        return vendorService.putBeforeImageComplaint(complaintId, employeeId, beforeImage)
    }

    override fun putProgressImageComplaint(
        complaintId: Int,
        employeeId: Int,
        progressImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel> {
        return vendorService.putProgressImageComplaint(complaintId, employeeId, progressImage)
    }

    override fun putAfterImageComplaint(
        complaintId: Int,
        employeeId: Int,
        afterImage: MultipartBody.Part
    ): Single<ProcessComplaintResponseModel> {
        return vendorService.putAfterImageComplaint(complaintId, employeeId, afterImage)
    }

    override fun putSubmitComplaint(
        complaintId: Int,
        employeeId: Int
    ): Single<ProcessComplaintResponseModel> {
        return vendorService.putSubmitComplaint(complaintId, employeeId)
    }

    override fun getNotifHistoryLeader(
        employeeId: Int,
        projectCode: String,
        category: String
    ): Single<NotificationMidHistory> {
        return vendorService.getNotifHistoryLeader(employeeId, projectCode, category)
    }

    override fun getNotifHistoryOperator(
        employeeId: Int,
        projectCode: String,
        category: String
    ): Single<NotificationMidHistory> {
        return vendorService.getNotifHistoryOperator(employeeId, projectCode, category)
    }

    override fun getNotificationHistoryLeader(
        employeeId: Int,
        projectCode: String,
        category: String,
        page: Int
    ): Single<NotificationMidResponseModel> {
        return vendorService.getNotificationHistoryLeader(employeeId, projectCode, category, page)
    }

    override fun getNotificationHistoryOperator(
        employeeId: Int,
        projectCode: String,
        category: String,
        page: Int
    ): Single<NotificationMidResponseModel> {
        return vendorService.getNotificationHistoryOperator(employeeId, projectCode, category, page)
    }

    override fun putReadNotificationMid(
        notificationId: Int,
        employeeId: Int
    ): Single<ReadNotificationMidResponseModel> {
        return vendorService.putReadNotificationMid(employeeId, notificationId)
    }


}