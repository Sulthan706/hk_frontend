package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.service

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
import retrofit2.http.*

interface AuditService {

    @GET("/api/v1/inspection/audit/before-submit-todocheck")
    fun getListFormAudit(
        @Query("createdBy") createdBy: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String
    ): Single<ListFormAuditResponse>

    @PUT("/api/v1/inspection/audit/submit-audit-report")
    fun submitFormAudit(
        @Query("createdBy") createdBy: Int,
        @Query("projectCode") projectCode: String,
        @Query("periode") periode: String,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @GET("/api/v1/inspection/audit/list-before-submit")
    fun getListQualityAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("date") date: String
    ): Single<ListQualityAuditResponse>

    @PUT("/api/v1/inspection/audit/submit-audit-kualitas")
    fun submitResultQualityAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType")  auditType: String,
        @Query("resultScore") resultScore: String,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @GET("/api/v1/inspection/audit/list-form-questions")
    fun getListQuestionAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("questionType") questionType: String,
        @Query("date") date: String
    ): Single<ListQuestionAuditResponse>

    @Multipart
    @PUT("/api/v1/inspection/audit/submit-form-question")
    fun submitPenilaianAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("questionId") questionId: Int,
        @Query("questionType") questionType: String,
        @Query("formStatus") formStatus: String,
        @Query("formDescription") formDescription: String,
        @Part file: MultipartBody.Part,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @DELETE("/api/v1/inspection/audit/delete-form-questions")
    fun deletePenilaianAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("questionType") questionType: ArrayList<String>,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @GET("/api/v1/inspection/audit/list-scored-hasil-kerja")
    fun getListHasilKerja(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("date") date: String
    ): Single<ListWorkResultResponse>

    @Multipart
    @PUT("/api/v1/inspection/audit/submit-hasil-kerja")
    fun submitHasilKerja(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("idArea") idArea: Int,
        @Query("idObject") idObject: Int,
        @Part file: MultipartBody.Part,
        @Query("score") score: Int,
        @Query("description") description: String,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @GET("/api/v1/inspection/audit/list-report")
    fun getListLaporanAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("periode") periode: String,
        @Query("page") page: Int
    ): Single<ListLaporanAuditResponse>

    @GET("/api/v1/inspection/audit/detail-report")
    fun getDetailAudit(
        @Query("idReport") idReport: Int
    ): Single<DetailAuditResponse>

    @GET("/api/v1/inspection/audit/detail-report-kualitas")
    fun getDetailAuditKualitas(
        @Query("idAuditKualitas") idAuditKualitas: Int
    ): Single<DetailAuditKualitasResponse>

    @GET("/api/v1/inspection/audit/detail-report-question")
    fun getDetailAuditQuestion(
        @Query("idAuditKualitas") idAuditKualitas: Int,
        @Query("questionType") questionType: String
    ): Single<DetailAuditQuestionResponse>

    @GET("/api/v1/inspection/audit/detail-submit-question")
    fun getDetailSubmitPenilaian(
        @Query("idSubmitQuestion") idSubmitQuestion: Int
    ): Single<DetailSubmitPenilaianResponse>

    @Multipart
    @PUT("/api/v1/inspection/audit/update-form-question")
    fun updatePenilaianAudit(
        @Query("idSubmitForm") idSubmitForm: Int,
        @Query("formStatus") formStatus: String,
        @Query("formDescription") formDescription: String,
        @Part file: MultipartBody.Part
    ): Single<SubmitFormAuditResponse>

    @GET("/api/v1/inspection/audit/detail-report-hasilkerja")
    fun getListReportHasilKerja(
        @Query("idAuditKualitas") idAuditKualitas: Int,
        @Query("page") page: Int
    ): Single<ListLaporanAreaResponse>

    @PUT("/api/v1/inspection/audit/update-hasil-kerja")
    fun updateHasilKerjaAudit(
        @Query("idHasilKerja") idHasilKerja: Int,
        @Query("score") score: Int
    ): Single<SubmitFormAuditResponse>

    @DELETE("/api/v1/inspection/audit/delete-hasil-kerja")
    fun deleteHasilKerja(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("auditType") auditType: String,
        @Query("idArea") idArea: ArrayList<Int>,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @DELETE("/api/v1/inspection/audit/delete-audit-report")
    fun deleteLaporanAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String
    ): Single<SubmitFormAuditResponse>

    @GET("/api/v1/inspection/audit/check-status")
    fun getCekStatusAudit(
        @Query("createdBy") userId: Int,
        @Query("projectCode") projectCode: String,
        @Query("periode") periode: String
    ): Single<SubmitFormAuditResponse>

}