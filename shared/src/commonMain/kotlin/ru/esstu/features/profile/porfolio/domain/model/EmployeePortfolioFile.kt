package ru.esstu.features.profile.porfolio.domain.model

import ru.esstu.features.profile.porfolio.data.model.EmployeePortfolio

import kotlin.random.Random

sealed interface EmployeePortfolioFile {

    data class Education(
        val educationType: String,
        val institution: String,
        val qualification: String,
        val speciality: String,
        val receiptDate: String
    ) : EmployeePortfolioFile

    data class AddEducation(
        val type: String,
        val name: String,
        val length: Int?,
        val place: String,
        val receiptDate: String,
        val startDate: String,
        val endDate: String,
        val attachment: Attachment?
    ) : EmployeePortfolioFile


    data class Award(
        val awardName: String,
        val awardDate: String,
        val scaleAward: String,
        val type: String,
        val organization: String
    ) : EmployeePortfolioFile
}


fun EmployeePortfolio.EmployeeEducation.toEducation(): EmployeePortfolioFile.Education =
    EmployeePortfolioFile.Education(
        educationType = educationType?.name.orEmpty(),
        institution = institution?.name.orEmpty(),
        qualification = qualification?.name.orEmpty(),
        speciality = speciality?.name.orEmpty(),
        receiptDate = receiptDate.orEmpty()
    )

fun EmployeePortfolio.AddEducation.toAddEducation(): EmployeePortfolioFile.AddEducation =
    EmployeePortfolioFile.AddEducation(
        type = type?.name.orEmpty(),
        name = name.orEmpty(),
        length = length,
        place = place.orEmpty(),
        receiptDate = receiptDate.orEmpty(),
        startDate = startDate.orEmpty(),
        endDate = endDate.orEmpty(),
        attachment = fileCode?.let {
            Attachment(
                id = Random.nextInt(),
                fileUri = "$ASIC_STORAGE_DEFAULT_PATH$fileCode",
                localFileUri = null,
                name = name.orEmpty(),
                size = 0,
                type = ""
            )
        }
    )


fun EmployeePortfolio.Award.toAward(): EmployeePortfolioFile.Award =
    EmployeePortfolioFile.Award(
        awardName = awardName.orEmpty(),
        awardDate = awardDate.orEmpty(),
        scaleAward = scale?.name.orEmpty(),
        type = type?.name.orEmpty(),
        organization = organization?.shortName ?: organization?.name.orEmpty()
    )