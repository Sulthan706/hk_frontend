package com.hkapps.hygienekleen.features.features_management.report.model.mainreportcftalk

data class ContentComplaintCftalk(
    val complaintDescription: String,
    val complaintId: Int,
    val complaintImage: String,
    val complaintStatus: String,
    val complaintTitle: String,
    val createdDate: String,
    val projectCode: String,
    val projectName: String
)