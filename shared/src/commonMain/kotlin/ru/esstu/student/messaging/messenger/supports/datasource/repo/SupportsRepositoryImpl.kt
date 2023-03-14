package ru.esstu.student.messaging.messenger.supports.datasource.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper
import kotlin.coroutines.CoroutineContext






class SupportsRepositoryImpl(
    private val supportsApi: ISupportsApiRepository,
    private val supportsDB: ISupportsDbRepository
): ISupportsRepository {
    private val _supports = MutableSharedFlow<List<ConversationPreview>>()

    override val supports: KotlinNativeFlowWrapper<List<ConversationPreview>>
        get() = KotlinNativeFlowWrapper(_supports.asSharedFlow())

    override suspend fun refresh() {

        val recentDialogs = supportsDB.getSupports(10, 0)
        _supports.emit(recentDialogs)

        val loadedDialogs = supportsApi.getSupports(10, 0)

        if (loadedDialogs is Response.Success){
            supportsDB.clear()
            supportsDB.setSupports(loadedDialogs.data)
            _supports.emit(loadedDialogs.data)
        }

    }

    override suspend fun getNextPage(offset: Int) {
        val recentDialogs = supportsDB.getSupports(10, offset)

        if (recentDialogs.isEmpty()){
            val loadedDialogs = supportsApi.getSupports(10, offset)
            if (loadedDialogs is Response.Success){
                supportsDB.setSupports(loadedDialogs.data)
                _supports.emit(loadedDialogs.data)
            }
        }else{
            _supports.emit(recentDialogs)
        }
    }

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

}