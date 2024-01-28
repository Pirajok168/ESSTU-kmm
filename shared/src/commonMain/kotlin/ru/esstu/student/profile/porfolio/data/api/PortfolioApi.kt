package ru.esstu.student.profile.porfolio.data.api

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.porfolio.data.model.AttestationResponse
import ru.esstu.student.profile.porfolio.data.model.EmployeePortfolio
import ru.esstu.student.profile.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.porfolio.data.model.PortfolioTypeResponse


interface PortfolioApi {

    suspend fun getFilesStudentPortfolioByType(type: PortfolioTypeResponse):
            Response<List<PortfolioFileRequestResponse>>


    suspend fun getEducationEmployeePortfolio():
            Response<List<EmployeePortfolio.EmployeeEducation>>

    suspend fun getAddEducationEmployeePortfolio():
            Response<List<EmployeePortfolio.AddEducation>>

    suspend fun getAwardEmployeePortfolio():
            Response<List<EmployeePortfolio.Award>>

    suspend fun saveFilePortfolio(
        type: PortfolioFileRequestResponse,
        files: List<CachedFile> = emptyList()
    ): Response<PortfolioFileRequestResponse>

    suspend fun getAttestationMarks(): Response<List<AttestationResponse>>
}