package ru.esstu.student.profile.student.porfolio.awards.domain.repository

import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.awards.data.api.AwardsApi
import ru.esstu.student.profile.student.porfolio.awards.domain.model.Award
import ru.esstu.student.profile.student.porfolio.awards.domain.toAward

class AwardsRepositoryImpl(
    private val api: AwardsApi,
    private val auth: IAuthRepository
) : IAwardsRepository{
    override fun getAwards(): Flow<FlowResponse<List<Award>>> = flow<FlowResponse<List<Award>>>{
        auth.provideToken { token ->

            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw IOException("unsupported user category")
            emit(FlowResponse.Loading())

           // val cachedFiles = awardDao.getAwards(appUserId).map { it.toAward(cachedFilesDao.getCachedAttachment(it.id)) }

          /*  if (cachedFiles.isNotEmpty())
                emit(FlowResponse.Success(cachedFiles))*/

            val remoteFiles = auth.provideToken { type, access ->
                api.getAwards("$type $access").mapNotNull { it.toAward() }
            }

            when (remoteFiles) {
                is Response.Error ->
                    emit(FlowResponse.Error(remoteFiles.error))
                is Response.Success -> {
                  //  awardDao.updateFiles(remoteFiles.data.map { it.toEntity(appUserId) })
                    emit(FlowResponse.Success(remoteFiles.data))
                }
            }

            emit(FlowResponse.Loading(false))
        }
    }
}