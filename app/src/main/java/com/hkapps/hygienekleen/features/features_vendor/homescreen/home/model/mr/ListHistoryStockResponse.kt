package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

data class ListHistoryStockResponse(
    val code : Int,
    val status : String,
    val data : List<ListHistoryStockData>
)

data class ListHistoryStockData(
    val itemId : Int,
    val itemCode : String,
    val itemName : String,
    val quantity : Int,
    val unitCode : String,
    val usedQty : Int,
    val remainingQty : Int

)
