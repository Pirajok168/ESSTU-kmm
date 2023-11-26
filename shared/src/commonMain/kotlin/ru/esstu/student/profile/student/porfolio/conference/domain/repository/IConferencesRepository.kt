package ru.esstu.student.profile.student.porfolio.conference.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.profile.student.porfolio.conference.domain.model.Conference

interface IConferencesRepository {
    fun getConferences(): Flow<FlowResponse<List<Conference>>>
}