package ru.esstu.student.profile.porfolio.domain.repository

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.porfolio.domain.model.Attestation
import ru.esstu.student.profile.porfolio.domain.model.EmployeePortfolioFile
import ru.esstu.student.profile.porfolio.domain.model.EmployeePortfolioType
import ru.esstu.student.profile.porfolio.domain.model.StudentPortfolioFile
import ru.esstu.student.profile.porfolio.domain.model.StudentPortfolioType

interface IPortfolioRepository {
    suspend fun getFilesStudentPortfolioByType(type: StudentPortfolioType): Response<List<StudentPortfolioFile>>

    suspend fun getFilesEmployeePortfolioByType(type: EmployeePortfolioType): Response<List<EmployeePortfolioFile>>

    suspend fun saveFilePortfolio(
        type: StudentPortfolioType,
        files: List<CachedFile> = emptyList(),
        studentCode: String? = null,
        eventName: String? = null,
        eventStatus: String? = null,
        eventPlace: String? = null,
        eventStartDate: Long? = null,
        eventEndDate: Long? = null,
        eventUrl: String? = null,
        workName: String? = null,
        workType: String? = null,
        result: String? = null,
        coauthorsText: String? = null,
        fileCode: String? = null,
        status: String? = null,
    ): Response<PortfolioFileRequestResponse>

    suspend fun getAttestation(): Response<List<Attestation>>
}