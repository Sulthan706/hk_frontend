package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAudit.DetailAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditKualitas.DetailAuditKualitasResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditQuestion.DetailAuditQuestionResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailSubmitPenilaian.DetailSubmitPenilaianResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listFormAudit.ListFormAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listLaporanAudit.ListLaporanAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQualityAudit.ListQualityAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQuestionAudit.ListQuestionAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listWorkResult.ListWorkResultResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.submitFormAudit.SubmitFormAuditResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.ListLaporanAreaResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface AuditRepository {

    fun getListFormAudit(
        createdBy: Int,
        projectCode: String,
        date: String
    ): Single<ListFormAuditResponse>

    fun submitFormAudit(
        createdBy: Int,
        projectCode: String,
        periode: String,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun getListQualityAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        date: String
    ): Single<ListQualityAuditResponse>

    fun submitResultQualityAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        resultScore: String,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun getListQuestionAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionType: String,
        date: String
    ): Single<ListQuestionAuditResponse>

    fun submitPenilaianAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionId: Int,
        questionType: String,
        formStatus: String,
        formDescription: String,
        file: MultipartBody.Part,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun deletePenilaianAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionType: ArrayList<String>,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun getListHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        date: String
    ): Single<ListWorkResultResponse>

    fun submitHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: Int,
        idObject: Int,
        file: MultipartBody.Part,
        score: Int,
        description: String,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun getListLaporanAudit(
        userId: Int,
        projectCode: String,
        periode: String,
        page: Int
    ): Single<ListLaporanAuditResponse>

    fun getDetailAudit(
        idReport: Int
    ): Single<DetailAuditResponse>

    fun getDetailAuditKualitas(
        idAuditKualitas: Int
    ): Single<DetailAuditKualitasResponse>

    fun getDetailAuditQuestion(
        idAuditKualitas: Int,
        questionType: String
    ): Single<DetailAuditQuestionResponse>

    fun getDetailSubmitPenilaian(
        idSubmitQuestion: Int
    ): Single<DetailSubmitPenilaianResponse>

    fun updatePenilaianAudit(
        idSubmitForm: Int,
        formStatus: String,
        formDescription: String,
        file: MultipartBody.Part
    ): Single<SubmitFormAuditResponse>

    fun getListReportHasilKerja(
        idAuditKualitas: Int,
        page: Int
    ): Single<ListLaporanAreaResponse>

    fun updateHasilKerjaAudit(
        idHasilKerja: Int,
        score: Int
    ): Single<SubmitFormAuditResponse>

    fun deleteHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: ArrayList<Int>,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun deleteLaporanAudit(
        userId: Int,
        projectCode: String,
        date: String
    ): Single<SubmitFormAuditResponse>

    fun getCekStatusAudit(
        userId: Int,
        projectCode: String,
        periode: String
    ): Single<SubmitFormAuditResponse>

}