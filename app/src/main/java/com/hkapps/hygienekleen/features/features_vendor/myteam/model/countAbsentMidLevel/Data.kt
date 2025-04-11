package com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsentMidLevel

data class Data(
    val employeeId: Int,
    val projectCode: String,
    val countEmployee: Int,
    val countEmployeeBelumAbsen: Int,
    val employeeBelumAbsen: List<EmployeeBelumAbsen>
)