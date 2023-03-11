package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import io.github.aakira.napier.Napier
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.KotlinNativeFlowWrapper
import kotlin.coroutines.CoroutineContext






class ConversationsRepositoryImpl(
    private val dialogApi: IConversationsApiRepository,
    private val dialogDB: IConversationsDbRepository
): IConversationsRepository {
    private val _dialogs = MutableSharedFlow<List<ConversationPreview>>()

    override val conversations: KotlinNativeFlowWrapper<List<ConversationPreview>>
        get() = KotlinNativeFlowWrapper(_dialogs.asSharedFlow())

    override suspend fun refresh() {

        val recentDialogs = dialogDB.getConversations(10, 0)
        Napier.e("Данные бд - ${recentDialogs.size}")
        _dialogs.emit(recentDialogs)

        val loadedDialogs = dialogApi.getConversations(10, 0)

        if (loadedDialogs is Response.Success){
            dialogDB.clear()
            dialogDB.setConversations(loadedDialogs.data)
            _dialogs.emit(loadedDialogs.data)
        }

    }

    override suspend fun getNextPage(offset: Int) {
        val recentDialogs = dialogDB.getConversations(10, offset)

        if (recentDialogs.isEmpty()){
            val loadedDialogs = dialogApi.getConversations(10, offset)
            if (loadedDialogs is Response.Success){
                dialogDB.setConversations(loadedDialogs.data)
                _dialogs.emit(loadedDialogs.data)
            }
        }else{
            _dialogs.emit(recentDialogs)
        }
    }

    override val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

}