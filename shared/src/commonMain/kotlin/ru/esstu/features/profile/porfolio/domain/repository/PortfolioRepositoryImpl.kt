package ru.esstu.features.profile.porfolio.domain.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import ru.esstu.features.profile.porfolio.data.api.PortfolioApi
import ru.esstu.features.profile.porfolio.data.model.AttestationResponse
import ru.esstu.features.profile.porfolio.data.model.EmployeePortfolio
import ru.esstu.features.profile.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.features.profile.porfolio.data.model.PortfolioTypeResponse
import ru.esstu.features.profile.porfolio.domain.model.Attestation
import ru.esstu.features.profile.porfolio.domain.model.ControlType
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioFile
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioType
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioFile
import ru.esstu.features.profile.porfolio.domain.model.StudentPortfolioType
import ru.esstu.features.profile.porfolio.domain.model.Subject
import ru.esstu.features.profile.porfolio.domain.model.toAddEducation
import ru.esstu.features.profile.porfolio.domain.model.toAttachment
import ru.esstu.features.profile.porfolio.domain.model.toAward
import ru.esstu.features.profile.porfolio.domain.model.toEducation
import ru.esstu.student.messaging.entities.CachedFile
import kotlin.random.Random

class PortfolioRepositoryImpl(
    private val api: PortfolioApi
) : IPortfolioRepository {
    override suspend fun getFilesStudentPortfolioByType(type: StudentPortfolioType): Response<List<StudentPortfolioFile>> {
        val response = when (type) {
            StudentPortfolioType.ACHIEVEMENT -> PortfolioTypeResponse.ACHIEVEMENT
            StudentPortfolioType.AWARD -> PortfolioTypeResponse.AWARD
            StudentPortfolioType.CONFERENCE -> PortfolioTypeResponse.CONFERENCE
            StudentPortfolioType.CONTEST -> PortfolioTypeResponse.CONTEST
            StudentPortfolioType.EXHIBITION -> PortfolioTypeResponse.EXHIBITION
            StudentPortfolioType.SCIENCEREPORT -> PortfolioTypeResponse.SCIENCEREPORT
            StudentPortfolioType.WORK -> PortfolioTypeResponse.WORK
            StudentPortfolioType.TRAINEESHIP -> PortfolioTypeResponse.TRAINEESHIP
            StudentPortfolioType.REVIEWS -> PortfolioTypeResponse.REVIEWS
            StudentPortfolioType.THEME -> PortfolioTypeResponse.THEME
            StudentPortfolioType.SCIENCEWORK -> PortfolioTypeResponse.SCIENCEWORK
        }.run {
            api.getFilesStudentPortfolioByType(this)
        }


        return response.transform { it.toPortfolioFile() }
    }

    override suspend fun getFilesEmployeePortfolioByType(type: EmployeePortfolioType): Response<List<EmployeePortfolioFile>> {
        val response: Response<List<EmployeePortfolioFile>> = with(api) {
            when (type) {
                EmployeePortfolioType.Educations -> getEducationEmployeePortfolio()
                EmployeePortfolioType.AddEducation -> getAddEducationEmployeePortfolio()
                EmployeePortfolioType.Award -> getAwardEmployeePortfolio()
            }
        }
            .transform {
                it.map {
                    when (it) {
                        is EmployeePortfolio.AddEducation -> it.toAddEducation()
                        is EmployeePortfolio.EmployeeEducation -> it.toEducation()
                        is EmployeePortfolio.Award -> it.toAward()
                    }
                }
            }
        return response
    }

    override suspend fun saveFilePortfolio(
        type: StudentPortfolioType,
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
            StudentPortfolioType.ACHIEVEMENT -> PortfolioTypeResponse.ACHIEVEMENT
            StudentPortfolioType.AWARD -> PortfolioTypeResponse.AWARD
            StudentPortfolioType.CONFERENCE -> PortfolioTypeResponse.CONFERENCE
            StudentPortfolioType.CONTEST -> PortfolioTypeResponse.CONTEST
            StudentPortfolioType.EXHIBITION -> PortfolioTypeResponse.EXHIBITION
            StudentPortfolioType.SCIENCEREPORT -> PortfolioTypeResponse.SCIENCEREPORT
            StudentPortfolioType.WORK -> PortfolioTypeResponse.WORK
            StudentPortfolioType.TRAINEESHIP -> PortfolioTypeResponse.TRAINEESHIP
            StudentPortfolioType.REVIEWS -> PortfolioTypeResponse.REVIEWS
            StudentPortfolioType.THEME -> PortfolioTypeResponse.THEME
            StudentPortfolioType.SCIENCEWORK -> PortfolioTypeResponse.SCIENCEWORK
        }.run {
            api.saveFilePortfolio(
                PortfolioFileRequestResponse(
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
                ), files
            )
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
            eduBlock = it.eduBlock,
            eduYear = it.eduYear,
            nameMarks = it.nameMarks,
            summaryGrade = it.summaryGrade,
            countMarks = it.countMarks,
            controlType = it.controlType?.let {
                ControlType(it.name)
            },
            subject = it.subject?.let {
                Subject(it.name)
            }
        )
    }


private fun List<PortfolioFileRequestResponse>.toPortfolioFile(): List<StudentPortfolioFile> =
    map {
        when (it.type) {
            PortfolioTypeResponse.ACHIEVEMENT -> StudentPortfolioFile.Achievement(
                id = it.id ?: Random.nextInt(),
                status = it.eventStatus.toString(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                date = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime()
            )

            PortfolioTypeResponse.AWARD -> StudentPortfolioFile.Award(
                id = it.id ?: Random.nextInt(),
                status = it.eventStatus.toString(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                date = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime()
            )

            PortfolioTypeResponse.CONFERENCE -> StudentPortfolioFile.Conference(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                status = it.eventStatus.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                endDate = Instant.fromEpochMilliseconds(
                    it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
                theme = it.workName.orEmpty(),
                coauthors = it.coauthorsText.orEmpty()
            )

            PortfolioTypeResponse.CONTEST -> StudentPortfolioFile.Contest(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                status = it.eventStatus.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                endDate = Instant.fromEpochMilliseconds(
                    it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
                result = it.result.orEmpty()
            )

            PortfolioTypeResponse.EXHIBITION -> StudentPortfolioFile.Exhibition(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                status = it.status.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                endDate = Instant.fromEpochMilliseconds(
                    it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
                exhibit = it.workName.toString()
            )

            PortfolioTypeResponse.SCIENCEREPORT -> StudentPortfolioFile.ScienceReport(
                id = it.id ?: Random.nextInt(),
                status = null,
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
            )

            PortfolioTypeResponse.WORK -> StudentPortfolioFile.Work(
                id = it.id ?: Random.nextInt(),
                title = it.workName.orEmpty(),
                attachment = it.toAttachment(),
                type = it.workType.orEmpty()
            )

            PortfolioTypeResponse.TRAINEESHIP -> StudentPortfolioFile.Traineeship(
                id = it.id ?: Random.nextInt(),
                title = it.eventName.toString(),
                attachment = it.toAttachment(),
                startDate = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                endDate = Instant.fromEpochMilliseconds(
                    it.eventEndDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
                place = it.eventPlace.orEmpty(),
            )

            PortfolioTypeResponse.REVIEWS -> StudentPortfolioFile.Reviews(
                id = it.id ?: Random.nextInt(),
                title = it.workName.toString(),
                attachment = it.toAttachment(),
                type = it.workType.orEmpty()
            )

            PortfolioTypeResponse.THEME -> StudentPortfolioFile.Theme(
                id = it.id ?: Random.nextInt(),
                title = it.workName.toString(),
                attachment = it.toAttachment(),
            )

            PortfolioTypeResponse.SCIENCEWORK -> StudentPortfolioFile.ScienceWorks(
                id = it.id,
                title = it.workName.orEmpty(),
                attachment = it.toAttachment(),
                coauthors = it.coauthorsText.orEmpty(),
                type = it.workType.orEmpty()
            )

            null -> StudentPortfolioFile.Achievement(
                it.id ?: Random.nextInt(),
                it.eventStatus.toString(),
                it.eventName.toString(),
                it.toAttachment(),
                date = Instant.fromEpochMilliseconds(
                    it.eventStartDate ?: Clock.System.now().toEpochMilliseconds()
                ).toLocalDateTime(),
            )
        }
    }
