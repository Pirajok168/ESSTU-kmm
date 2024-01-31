package ru.esstu.features.messanger.appeal.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.appeal.domain.toAppeals
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.update.data.api.UpdatesApi


class AppealsUpdatesRepositoryImpl(
    private val api: UpdatesApi
) : IAppealUpdatesRepository {


    override suspend fun installObserving(): Flow<Response<List<ConversationPreview>>> = flow {
        while (true) {
            val callTimestamp = Clock.System.now().toEpochMilliseconds()
            val result = api.getUpdates(callTimestamp)

            when (result) {
                is Response.Error -> {
                    emit(Response.Error(result.error))
                    if (result.error.message != "timeout") delay(1000L)
                }

                is Response.Success -> {
                    emit(Response.Success(result.data.toAppeals()))
                }
            }
        }
    }


}