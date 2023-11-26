package ru.esstu.student.profile.student.porfolio.conference.domain

import com.soywiz.klock.DateTime
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.toAttachment
import ru.esstu.student.profile.student.porfolio.conference.domain.model.Conference
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

suspend fun PortfolioFileRequestResponse.toConference(): Conference? {
    return Conference(
        id = id ?: return null,
        title = eventName.orEmpty(),
        place = eventPlace.orEmpty(),
        attachment = toAttachment(),
        startDate = DateTime(eventStartDate ?: DateTime.nowUnixLong()),
        endDate = DateTime(eventEndDate ?: DateTime.nowUnixLong()),
        status = eventStatus.orEmpty(),
        coauthors = coauthorsText.orEmpty(),
        theme = workName.orEmpty(),
    )
}
