package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.chemicalsComplaintInternal

data class ChemicalsComplaintResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)