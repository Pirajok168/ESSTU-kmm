package ru.esstu.android.student.messaging.group_chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.esstu.ESSTUSdk2
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.group_chat.datasources.di.groupChatModule
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatRepository
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatUpdateRepository
import ru.esstu.student.messaging.group_chat.entities.CachedFile
import ru.esstu.student.messaging.group_chat.entities.Conversation
import ru.esstu.student.messaging.group_chat.entities.NewUserMessage
import ru.esstu.student.messaging.group_chat.entities.SentUserMessage
import ru.esstu.student.messaging.group_chat.util.toSentUserMessage


data class GroupChatState(
    val isHeaderLoading: Boolean = false,

    val conversation: Conversation? = null,

    val pageSize: Int = 20,
    val pages: List<Message> = emptyList(),
    val isPageLoading: Boolean = false,

    val isEndReached: Boolean = false,
    val error: ResponseError? = null,


    val message: NewUserMessage = NewUserMessage(),

    //только-что отправленные сообщения
    val sentMessages: List<SentUserMessage> = emptyList()
)

sealed class GroupChatEvents {
    data class PassConversation(val id: Int) : GroupChatEvents()
    object CancelObserver : GroupChatEvents()
    object NextPage : GroupChatEvents()

    data class PassMessage(val message: String) : GroupChatEvents()
    data class PassReplyMessage(val message: Message) : GroupChatEvents()
    data class PassAttachments(val attachments: List<CachedFile>) : GroupChatEvents()
    data class RemoveAttachment(val attachment: CachedFile) : GroupChatEvents()
    object RemoveReplyMessage : GroupChatEvents()

    object SendMessage : GroupChatEvents()
    data class ResendMessage(val message: SentUserMessage) : GroupChatEvents()

    data class DownloadAttachment(val messageId: Long, val attachment: MessageAttachment) : GroupChatEvents()
    data class UpdateAttachment(val messageId: Long, val attachment: MessageAttachment) : GroupChatEvents()
}


class GroupChatViewModel  constructor(
    private val groupChatRepository: IGroupChatRepository = ESSTUSdk2.groupChatModule.repo,
    private val dialogChatUpdateRepository: IGroupChatUpdateRepository = ESSTUSdk2.groupChatModule.updates,
   // private val fileDownloadRepository: IFileDownloadRepository
) : ViewModel() {

    var dialogChatState by mutableStateOf(GroupChatState())
        private set

    fun onEvent(event: GroupChatEvents) {
        when (event) {
            is GroupChatEvents.PassConversation -> viewModelScope.launch { onPassOpponent(event.id) }
            is GroupChatEvents.CancelObserver -> onCancelObserver()
            is GroupChatEvents.NextPage -> viewModelScope.launch { paginator.loadNext() }

            is GroupChatEvents.PassMessage -> withCachedMsg { dialogChatState = dialogChatState.copy(message = dialogChatState.message.copy(text = event.message)) }
            is GroupChatEvents.PassReplyMessage -> withCachedMsg { dialogChatState = dialogChatState.copy(message = dialogChatState.message.copy(replyMessage = event.message)) }
            is GroupChatEvents.PassAttachments -> withCachedMsg { onPassAttachments(event.attachments) }
            is GroupChatEvents.RemoveAttachment -> withCachedMsg { onRemoveAttachment(event.attachment) }
            is GroupChatEvents.RemoveReplyMessage -> withCachedMsg { dialogChatState = dialogChatState.copy(message = dialogChatState.message.copy(replyMessage = null)) }

            is GroupChatEvents.SendMessage -> withCachedMsg { onSendMessage() }
            is GroupChatEvents.ResendMessage -> viewModelScope.launch { onResendMessage(event.message) }

            is GroupChatEvents.DownloadAttachment -> {
                //fileDownloadRepository.downloadFile(event.attachment)
            }
            is GroupChatEvents.UpdateAttachment -> viewModelScope.launch { onUpdateAttachment(event.messageId, event.attachment) }
        }
    }

    private val paginator: Paginator<Int, Message> = Paginator(
        initialKey = 0,

        onRequest = { key ->
            val conv = dialogChatState.conversation ?: return@Paginator Response.Error(ResponseError())

            groupChatRepository.getPage(conv.id, dialogChatState.pageSize, key)
        },

        onRefresh = { page ->
            dialogChatState = dialogChatState.copy(pages = page, error = null, isEndReached = page.isEmpty())

            val conv = dialogChatState.conversation ?: return@Paginator

            installObserver(conv.id, page.lastOrNull { it.status == DeliveryStatus.DELIVERED }?.id ?: page.firstOrNull()?.id ?: 0)

            attachErredMessages(conv.id)

            dialogChatState = dialogChatState.copy(message = groupChatRepository.getUserMessage(conv.id))

            updatePreview(page.firstOrNull())
        },
        onNext = { _, page ->
            dialogChatState = dialogChatState.copy(pages = dialogChatState.pages + page, error = null, isEndReached = page.isEmpty())
        },

        onLoad = { dialogChatState = dialogChatState.copy(isPageLoading = it) },
        getNextKey = { _, _ -> dialogChatState.pages.size },
        onError = { dialogChatState = dialogChatState.copy(error = it) }
    )

    private var updatesObserver: Job? = null

    private fun onCancelObserver() {
        if (updatesObserver?.isCancelled != true)
            updatesObserver?.cancel()

        dialogChatState = dialogChatState.copy(conversation = null, message = NewUserMessage(), sentMessages = emptyList(), pages = emptyList())
    }

    private fun installObserver(convId: Int, lastMessageId: Long) {
        if (updatesObserver?.isCancelled != true)
            updatesObserver?.cancel()

        val updatesFlow = dialogChatUpdateRepository
            .collectUpdates(convId = convId, lastMessageId = lastMessageId)
            .cancellable()

        updatesObserver = viewModelScope.launch {
            updatesFlow.collect { response ->
                when (response) {
                    is Response.Error -> dialogChatState = dialogChatState.copy(error = response.error)
                    is Response.Success -> {
                        dialogChatState = dialogChatState.copy(error = null)
                        if (response.data.isEmpty()) return@collect

                        val updates = response.data

                        groupChatRepository.setMessages(convId, updates)
                        dialogChatState = dialogChatState.copy(
                            pages = (updates + dialogChatState.pages).distinctBy { it.id },
                            sentMessages = dialogChatState.sentMessages.filter { sent -> updates.none { upd -> upd.id == sent.id } }
                        )

                        updatePreview(updates.firstOrNull())
                    }
                }
            }
        }
    }

    private suspend fun updatePreview(message: Message?){
        val conv = dialogChatState.conversation ?: return
        val msg = message?:return
        groupChatRepository.updateLastMessageOnPreview(convId = conv.id, message = msg)
    }

    private suspend fun attachErredMessages(convId: Int) {
        dialogChatState = dialogChatState.copy(sentMessages = groupChatRepository.getErredMessages(convId))

        dialogChatState.sentMessages.forEach { msg ->
            if (msg.status == DeliveryStatus.ERRED)
                onEvent(GroupChatEvents.ResendMessage(msg))
        }
    }

    private suspend fun onPassOpponent(id: Int) {
        onCancelObserver()

        groupChatRepository.getHeader(id).collect { response ->
            when (response) {
                is FlowResponse.Error -> dialogChatState = dialogChatState.copy(error = response.error)
                is FlowResponse.Loading -> dialogChatState = dialogChatState.copy(isHeaderLoading = response.isLoading)
                is FlowResponse.Success -> {
                    val isHeaderUpdating = dialogChatState.conversation?.id == response.data.id

                    dialogChatState = dialogChatState.copy(conversation = response.data, error = null)

                    if (!isHeaderUpdating) paginator.refresh()
                }
            }
        }
    }

    private fun onPassAttachments(attachments: List<CachedFile>) {
        val newAttachments = (dialogChatState.message.attachments + attachments).distinctBy { cachedFile -> cachedFile.uri }

        dialogChatState = dialogChatState
            .copy(message = dialogChatState.message.copy(attachments = newAttachments))
    }


    private fun onRemoveAttachment(attachment: CachedFile) {

        dialogChatState = dialogChatState
            .copy(message = dialogChatState.message.copy(attachments = dialogChatState.message.attachments - attachment))

    }

    private fun withCachedMsg(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()

            val conv = dialogChatState.conversation ?: return@launch
            val message = dialogChatState.message
            groupChatRepository.updateUserMessage(convId = conv.id, message = message)
        }
    }

    private val mutex = Mutex()

    private suspend fun onSendMessage() {

        val conv = dialogChatState.conversation ?: return

        val sentUserMessage = dialogChatState.message.toSentUserMessage()

        dialogChatState = dialogChatState.copy(message = NewUserMessage(), sentMessages = listOf(sentUserMessage) + dialogChatState.sentMessages)

        val result = groupChatRepository.sendMessage(
            convId = conv.id,
            message = sentUserMessage.text,
            attachments = sentUserMessage.attachments,
            replyMessage = sentUserMessage.replyMessage
        )

        mutex.withLock {
            dialogChatState = when (result) {
                is Response.Error ->
                    dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                        if (sent == sentUserMessage) {
                            val erred = sent.copy(status = DeliveryStatus.ERRED)
                            groupChatRepository.setErredMessage(conv.id, erred)
                            erred
                        } else
                            sent
                    })

                is Response.Success ->
                    dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                        if (sent == sentUserMessage) {
                            val success = sent.copy(id = result.data, status = DeliveryStatus.DELIVERED)
                            groupChatRepository.delErredMessage(sent.id)
                            success
                        } else
                            sent
                    })
            }
        }
    }


    private suspend fun onResendMessage(message: SentUserMessage) {

        val conv = dialogChatState.conversation ?: return

        val updatedMessage = message.copy(status = DeliveryStatus.SENT)

        dialogChatState = dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
            if (sent == message)
                updatedMessage
            else
                sent
        })

        val result = groupChatRepository.sendMessage(
            convId = conv.id,
            message = message.text,
            attachments = message.attachments,
            replyMessage = message.replyMessage
        )

        mutex.withLock {
            dialogChatState = when (result) {
                is Response.Error ->
                    dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                        if (sent == updatedMessage) {
                            val erred = sent.copy(status = DeliveryStatus.ERRED)
                            groupChatRepository.setErredMessage(conv.id, erred)
                            erred
                        } else
                            sent
                    })

                is Response.Success ->
                    dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                        if (sent == updatedMessage) {
                            val success = sent.copy(id = result.data, status = DeliveryStatus.DELIVERED)
                            groupChatRepository.delErredMessage(sent.id)
                            success
                        } else
                            sent
                    })
            }
        }
    }


    private suspend fun onUpdateAttachment(messageId: Long, attachment: MessageAttachment) {
      /*  mutex.withLock {

            val messageIndex = dialogChatState.pages.indexOfFirst { it.id == messageId }
            if (messageIndex == -1) return

            val attachments = dialogChatState.pages[messageIndex].attachments.toMutableList()

            val oldAttachmentIndex = attachments.indexOfFirst { it.id == attachment.id }
            if (oldAttachmentIndex == -1) return

            attachments[oldAttachmentIndex] = attachment
            val updatedAttachments = attachments.toImmutableList()

            val updatedMessage = dialogChatState.pages[messageIndex].copy(attachments = updatedAttachments)
            val pages = dialogChatState.pages.toMutableList()
            pages[messageIndex] = updatedMessage
            val updatedPages = pages.toImmutableList()

            dialogChatState = dialogChatState.copy(pages = updatedPages)
        }

        groupChatRepository.updateFile(messageId, attachment)*/
    }
}