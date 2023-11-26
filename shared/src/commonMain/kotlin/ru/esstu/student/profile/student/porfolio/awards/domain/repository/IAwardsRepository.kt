package ru.esstu.student.profile.student.porfolio.awards.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.profile.student.porfolio.awards.domain.model.Award

interface IAwardsRepository {
    fun getAwards(): Flow<FlowResponse<List<Award>>>
}