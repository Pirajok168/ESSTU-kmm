package ru.esstu.student.profile.student.porfolio.domain.repository

import com.soywiz.klock.DateTime
import kotlinx.coroutines.delay
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.news.announcement.datasources.toAnnouncements
import ru.esstu.student.news.announcement.db.announcement.toTimeStamp
import ru.esstu.student.news.announcement.db.announcement.toTimeStampEntity
import ru.esstu.student.profile.student.porfolio.data.api.PortfolioApi
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import ru.esstu.student.profile.student.porfolio.domain.model.toAttachment
import kotlin.random.Random

class PortfolioRepositoryImpl(
    private val api: PortfolioApi,
    private val auth: IAuthRepository
) : IPortfolioRepository {
    override suspend fun getFilesPortfolioByType(type: PortfolioType): Response<List<PortfolioFile>> {
        val response = auth.provideToken { token ->

            when (type) {
                PortfolioType.ACHIEVEMENT -> PortfolioTypeResponse.ACHIEVEMENT
                PortfolioType.AWARD -> PortfolioTypeResponse.AWARD
                PortfolioType.CONFERENCE -> PortfolioTypeResponse.CONFERENCE
                PortfolioType.CONTEST -> PortfolioTypeResponse.CONTEST
                PortfolioType.EXHIBITION -> PortfolioTypeResponse.EXHIBITION
                PortfolioType.SCIENCEREPORT -> PortfolioTypeResponse.SCIENCEREPORT
                PortfolioType.WORK -> PortfolioTypeResponse.WORK
                PortfolioType.TRAINEESHIP -> PortfolioTypeResponse.TRAINEESHIP
                PortfolioType.REVIEWS -> PortfolioTypeResponse.REVIEWS
                PortfolioType.THEME -> PortfolioTypeResponse.THEME
                PortfolioType.SCIENCEWORK -> PortfolioTypeResponse.SCIENCEWORK
            }.run {
                api.getFilesPortfolioByType(this, token.access)
            }
        }


        return when (response) {
            is Response.Error -> {
                Response.Error(response.error)
            }

            is Response.Success -> {
                Response.Success(
                    response.data.transform()
                )
            }
        }
    }
}


private fun List<PortfolioFileRequestResponse>.transform(): List<PortfolioFile> =
    map {
        when (it.type) {
            PortfolioTypeResponse.ACHIEVEMENT -> PortfolioFile.Achievement(
                id = it.id ?: Random.nextInt(),
                status = it.status.toString(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )

            PortfolioTypeResponse.AWARD -> PortfolioFile.Award(
                id = it.id ?: Random.nextInt(),
                status = it.status.toString(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.CONFERENCE -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.CONTEST -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.EXHIBITION -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.SCIENCEREPORT -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.WORK -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.TRAINEESHIP -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.REVIEWS -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.THEME -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            PortfolioTypeResponse.SCIENCEWORK -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
            null -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.status.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = DateTime(it.eventStartDate ?: DateTime.now().unixMillisLong)
            )
        }
    }
