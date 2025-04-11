package com.hkapps.hygienekleen.features.features_management.report.model.mainreportctalk

data class ContentComplaintCtalk(
    val complaintDescription: String,
    val complaintId: Int,
    val complaintImage: Any,
    val complaintStatus: String,
    val complaintTitle: String,
    val complaintTitleId: Int,
    val createdDate: String,
    val projectCode: String,
    val projectName: String
)