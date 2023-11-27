package ru.esstu.student.profile.student.porfolio.data.api

import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse

interface PortfolioApi {

    suspend fun getFilesPortfolioByType(type: PortfolioTypeResponse, authToken: String): List<PortfolioFileRequestResponse>
}