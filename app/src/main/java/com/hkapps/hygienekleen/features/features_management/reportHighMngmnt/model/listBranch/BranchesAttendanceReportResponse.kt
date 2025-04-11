package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listBranch

data class BranchesAttendanceReportResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)