package com.hkapps.academy.features.features_trainer.myclass.data.service

import retrofit2.http.GET
import retrofit2.http.Url

interface PdfApi {
    @GET
    fun getPdf(@Url url: String): String
}