package ru.esstu.student.profile.student.porfolio.conference.domain.repository

import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.awards.data.api.AwardsApi
import ru.esstu.student.profile.student.porfolio.conference.data.api.ConferenceApi
import ru.esstu.student.profile.student.porfolio.conference.domain.model.Conference
import ru.esstu.student.profile.student.porfolio.conference.domain.toConference

class ConferencesRepositoryImpl(
    private val api: ConferenceApi,
    private val auth: IAuthRepository
): IConferencesRepository {
    override fun getConferences() = flow<FlowResponse<List<Conference>>> {
        auth.provideToken { token ->

            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw IOException("unsupported user category")
            emit(FlowResponse.Loading())

           // val cachedFiles = conferenceDao.getConferences(appUserId).map { it.toConference(cachedFilesDao.getCachedAttachment(it.id)) }

            /*if (cachedFiles.isNotEmpty())
                emit(FlowResponse.Success(cachedFiles))*/

            val remoteFiles = auth.provideToken { type, access ->
                api.getConferences("$type $access").mapNotNull { it.toConference() }
            }

            when (remoteFiles) {
                is Response.Error ->
                    emit(FlowResponse.Error(remoteFiles.error))
                is Response.Success -> {
                   // conferenceDao.updateFiles(remoteFiles.data.map { it.toEntity(appUserId) })
                    emit(FlowResponse.Success(remoteFiles.data))
                }
            }

            emit(FlowResponse.Loading(false))
        }
    }
}