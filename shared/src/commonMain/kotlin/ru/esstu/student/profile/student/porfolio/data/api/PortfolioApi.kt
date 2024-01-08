package ru.esstu.student.profile.student.porfolio.data.api

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.data.model.AttestationResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse

interface PortfolioApi {

    suspend fun getFilesPortfolioByType(type: PortfolioTypeResponse):
            Response<List<PortfolioFileRequestResponse>>

    suspend fun saveFilePortfolio(
        type: PortfolioFileRequestResponse,
        files: List<CachedFile> = emptyList()
    ): Response<PortfolioFileRequestResponse>

    suspend fun getAttestationMarks(): Response<List<AttestationResponse>>
}