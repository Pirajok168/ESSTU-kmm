package ru.esstu.student.profile.student.porfolio.domain.repository

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

interface IPortfolioRepository {
    suspend fun getFilesPortfolioByType(type: PortfolioType): Response<List<PortfolioFile>>

    suspend fun saveFilePortfolio(
        type: PortfolioType,
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
}