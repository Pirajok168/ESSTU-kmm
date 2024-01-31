package ru.esstu.features.profile.porfolio.domain.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.profile.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.features.profile.porfolio.domain.model.Attestation
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioFile
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioType
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioFile
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioType
import ru.esstu.student.messaging.entities.CachedFile

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