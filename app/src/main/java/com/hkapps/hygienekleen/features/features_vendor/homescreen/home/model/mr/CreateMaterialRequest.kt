package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CreateMaterialRequest(
    val projectCode : String,
    val month : Int,
    val year : Int,
    val employeeId : Int,
    val tambahMaterialRequestsList : List<MaterialRequestSend>
)

data class MaterialRequest(
    var id : Int = 0,
    var name : String = "",
    var unit : String = "",
    var quantity : Int = 0,
    var satuan : Int = 0,
)


@Parcelize
data class MaterialRequestSend(
    var idItem: Int = 0,
    var qtyRequest: Int = 0,
    var idSatuan: Int = 0
) : Parcelable