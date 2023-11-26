package ru.esstu.student.profile.student.porfolio.achivement.domain

import com.soywiz.klock.DateTime
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Achievement
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Attachment
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.toAttachment
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import kotlin.random.Random

suspend fun PortfolioFileRequestResponse.toAchievement(): Achievement? {
    val fileExt = eventName.orEmpty().split('.').let { if (it.size > 1) it.last() else "" }
    return Achievement(
        id = id ?: return null,
        title = eventName.orEmpty(),
        status = eventStatus.orEmpty(),
        attachment = toAttachment(),
        date = DateTime(eventStartDate ?: DateTime.now().unixMillisLong)
    )
}

const val ASIC_STORAGE_DEFAULT_PATH = "https://esstu.ru/aicstorages/publicDownload/"