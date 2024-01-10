package ru.esstu.student.profile.student.porfolio.domain.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.data.api.PortfolioApi
import ru.esstu.student.profile.student.porfolio.data.model.AttestationResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse
import ru.esstu.student.profile.student.porfolio.domain.model.Attestation
import ru.esstu.student.profile.student.porfolio.domain.model.ControlType
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import ru.esstu.student.profile.student.porfolio.domain.model.Subject
import ru.esstu.student.profile.student.porfolio.domain.model.toAttachment
import kotlin.random.Random

class PortfolioRepositoryImpl(
    private val api: PortfolioApi
) : IPortfolioRepository {
    override suspend fun getFilesPortfolioByType(type: PortfolioType): Response<List<PortfolioFile>> {
        val response =  when (type) {
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
            api.getFilesPortfolioByType(this)
        }


        return response.transform { it.toPortfolioFile()  }
    }

    override suspend fun saveFilePortfolio(
        type: PortfolioType,
        files: List<CachedFile>,
        studentCode: String?,
        eventName: String?,
        eventStatus: String?,
        eventPlace: String?,
        eventStartDate: Long?,
        eventEndDate: Long?,
        eventUrl: String?,
        workName: String?,
        workType: String?,
        result: String?,
        coauthorsText: String?,
        fileCode: String?,
        status: String?
    ): Response<PortfolioFileRequestResponse> {
        val response = when (type) {
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
            api.saveFilePortfolio(PortfolioFileRequestResponse(
                id = null,
                type = this,
                studentCode = studentCode,
                eventName = eventName,
                eventStatus = eventStatus,
                eventPlace = eventPlace,
                eventStartDate = eventStartDate,
                eventEndDate = eventEndDate,
                eventUrl = eventUrl,
                workName = workName,
                workType = workType,
                result = result,
                coauthorsText = coauthorsText,
                fileCode = fileCode,
                status = status
            ), files)
        }
        return response
    }

    override suspend fun getAttestation(): Response<List<Attestation>> {
        val response = api.getAttestationMarks()
            .transform {
                it.transformAttestation()
                    .sortedWith((compareBy({ it.eduYear }, { it.eduBlock })))
            }
        return response
    }
}

private fun List<AttestationResponse>.transformAttestation(): List<Attestation> =
    map {
        Attestation(
            year = it.year,
            eduBlock = it.eduBlock ,
            eduYear = it.eduYear,
            nameMarks = it.nameMarks,
            summaryGrade = it.summaryGrade,
            countMarks = it.countMarks,
            controlType =  it.controlType?.let {
                ControlType(it.name)
            },
            subject = it.subject?.let {
                Subject(it.name)
            }
        )
    }


private fun List<PortfolioFileRequestResponse>.toPortfolioFile(): List<PortfolioFile> =
    map {
        when (it.type) {
            PortfolioTypeResponse.ACHIEVEMENT -> PortfolioFile.Achievement(
                id = it.id ?: Random.nextInt(),
                status = it.eventStatus.toString(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                date = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime()
            )

            PortfolioTypeResponse.AWARD -> PortfolioFile.Award(
                id = it.id ?: Random.nextInt(),
                status = it.eventStatus.toString(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                date = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime()
            )
            PortfolioTypeResponse.CONFERENCE -> PortfolioFile.Conference(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                status = it.eventStatus.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                endDate = Instant.fromEpochMilliseconds(it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
                theme = it.workName.orEmpty(),
                coauthors = it.coauthorsText.orEmpty()
            )
            PortfolioTypeResponse.CONTEST -> PortfolioFile.Contest(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                status = it.eventStatus.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                endDate =  Instant.fromEpochMilliseconds(it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
                result = it.result.orEmpty()
            )
            PortfolioTypeResponse.EXHIBITION -> PortfolioFile.Exhibition(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                status = it.status.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                endDate =  Instant.fromEpochMilliseconds(it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
                exhibit = it.workName.toString()
            )

            PortfolioTypeResponse.SCIENCEREPORT -> PortfolioFile.ScienceReport(
                id = it.id ?: Random.nextInt(),
                status = null,
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
            )
            PortfolioTypeResponse.WORK -> PortfolioFile.Work(
                id = it.id ?: Random.nextInt(),
                title = it.workName.orEmpty(),
                attachment = it.toAttachment(),
                type = it.workType.orEmpty()
            )
            PortfolioTypeResponse.TRAINEESHIP -> PortfolioFile.Traineeship(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                endDate =  Instant.fromEpochMilliseconds(it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
            )
            PortfolioTypeResponse.REVIEWS -> PortfolioFile.Reviews(
                id = it.id ?: Random.nextInt(),
                title = it.workName.toString(),
                attachment = it.toAttachment(),
                type = it.workType.orEmpty()
            )
            PortfolioTypeResponse.THEME -> PortfolioFile.Theme(
                id = it.id ?: Random.nextInt(),
                title = it.workName.toString(),
                attachment = it.toAttachment(),
            )
            PortfolioTypeResponse.SCIENCEWORK -> PortfolioFile.ScienceWorks(
                id = it.id,
                title = it.workName.orEmpty(),
                attachment = it.toAttachment(),
                coauthors = it.coauthorsText.orEmpty(),
                type = it.workType.orEmpty()
            )
            null -> PortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.eventStatus.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = Instant.fromEpochMilliseconds(it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()).toLocalDateTime(),
            )
        }
    }
