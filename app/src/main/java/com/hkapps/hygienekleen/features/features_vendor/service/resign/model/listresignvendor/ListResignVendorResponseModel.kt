package com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor

data class ListResignVendorResponseModel(
    val code: Int,
    val `data`: List<DataListResignVendor>,
    val status: String
)