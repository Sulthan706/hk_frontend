package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.remote.AuditDataSource
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
import javax.inject.Inject

class AuditRepositoryImpl @Inject constructor(private val dataSource: AuditDataSource): AuditRepository {

    override fun getListFormAudit(
        createdBy: Int,
        projectCode: String,
        date: String
    ): Single<ListFormAuditResponse> {
        return dataSource.getListFormAudit(createdBy, projectCode, date)
    }

    override fun submitFormAudit(
        createdBy: Int,
        projectCode: String,
        periode: String,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.submitFormAudit(createdBy, projectCode, periode, date)
    }

    override fun getListQualityAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        date: String
    ): Single<ListQualityAuditResponse> {
        return dataSource.getListQualityAudit(userId, projectCode, auditType, date)
    }

    override fun submitResultQualityAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        resultScore: String,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.submitResultQualityAudit(userId, projectCode, auditType, resultScore, date)
    }

    override fun getListQuestionAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionType: String,
        date: String
    ): Single<ListQuestionAuditResponse> {
        return dataSource.getListQuestionAudit(userId, projectCode, auditType, questionType, date)
    }

    override fun submitPenilaianAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionId: Int,
        questionType: String,
        formStatus: String,
        formDescription: String,
        file: MultipartBody.Part,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.submitPenilaianAudit(userId, projectCode, auditType, questionId, questionType, formStatus, formDescription, file, date)
    }

    override fun deletePenilaianAudit(
        userId: Int,
        projectCode: String,
        auditType: String,
        questionType: ArrayList<String>,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.deletePenilaianAudit(userId, projectCode, auditType, questionType, date)
    }

    override fun getListHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        date: String
    ): Single<ListWorkResultResponse> {
        return dataSource.getListHasilKerja(userId, projectCode, auditType, date)
    }

    override fun submitHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: Int,
        idObject: Int,
        file: MultipartBody.Part,
        score: Int,
        description: String,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.submitHasilKerja(userId, projectCode, auditType, idArea, idObject, file, score, description, date)
    }

    override fun getListLaporanAudit(
        userId: Int,
        projectCode: String,
        periode: String,
        page: Int
    ): Single<ListLaporanAuditResponse> {
        return dataSource.getListLaporanAudit(userId, projectCode, periode, page)
    }

    override fun getDetailAudit(idReport: Int): Single<DetailAuditResponse> {
        return dataSource.getDetailAudit(idReport)
    }

    override fun getDetailAuditKualitas(idAuditKualitas: Int): Single<DetailAuditKualitasResponse> {
        return dataSource.getDetailAuditKualitas(idAuditKualitas)
    }

    override fun getDetailAuditQuestion(
        idAuditKualitas: Int,
        questionType: String
    ): Single<DetailAuditQuestionResponse> {
        return dataSource.getDetailAuditQuestion(idAuditKualitas, questionType)
    }

    override fun getDetailSubmitPenilaian(idSubmitQuestion: Int): Single<DetailSubmitPenilaianResponse> {
        return dataSource.getDetailSubmitPenilaian(idSubmitQuestion)
    }

    override fun updatePenilaianAudit(
        idSubmitForm: Int,
        formStatus: String,
        formDescription: String,
        file: MultipartBody.Part
    ): Single<SubmitFormAuditResponse> {
        return dataSource.updatePenilaianAudit(idSubmitForm, formStatus, formDescription, file)
    }

    override fun getListReportHasilKerja(
        idAuditKualitas: Int,
        page: Int
    ): Single<ListLaporanAreaResponse> {
        return dataSource.getListReportHasilKerja(idAuditKualitas, page)
    }

    override fun updateHasilKerjaAudit(
        idHasilKerja: Int,
        score: Int
    ): Single<SubmitFormAuditResponse> {
        return dataSource.updateHasilKerjaAudit(idHasilKerja, score)
    }

    override fun deleteHasilKerja(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: ArrayList<Int>,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.deleteHasilKerja(userId, projectCode, auditType, idArea, date)
    }

    override fun deleteLaporanAudit(
        userId: Int,
        projectCode: String,
        date: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.deleteLaporanAudit(userId, projectCode, date)
    }

    override fun getCekStatusAudit(
        userId: Int,
        projectCode: String,
        periode: String
    ): Single<SubmitFormAuditResponse> {
        return dataSource.getCekStatusAudit(userId, projectCode, periode)
    }

}