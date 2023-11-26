package ru.esstu.student.profile.student.porfolio.awards.domain

import com.soywiz.klock.DateTime
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.toAttachment
import ru.esstu.student.profile.student.porfolio.awards.domain.model.Award
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

suspend fun PortfolioFileRequestResponse.toAward(): Award? {
    return Award(
        id = id ?: return null,
        title = eventName.orEmpty(),
        status = eventStatus.orEmpty(),
        attachment = toAttachment(),
        date = DateTime(eventStartDate ?: DateTime.now().unixMillisLong)
    )
}