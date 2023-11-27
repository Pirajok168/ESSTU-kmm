package ru.esstu.student.profile.student.porfolio.domain.repository

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

interface IPortfolioRepository {
    suspend fun getFilesPortfolioByType(type: PortfolioType): Response<List<PortfolioFile>>
}