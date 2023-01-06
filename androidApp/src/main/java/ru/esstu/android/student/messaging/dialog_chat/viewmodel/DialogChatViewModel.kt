package ru.esstu.android.student.messaging.dialog_chat.viewmodel

import android.util.Log
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
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.repo.IAccountInfoApiRepository
import ru.esstu.domain.modules.account.di.accountModule
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.paginator.Paginator
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.dialog_chat.datasources.di.dialogChatModule
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatRepository
import ru.esstu.student.messaging.dialog_chat.datasources.repo.IDialogChatUpdateRepository

import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import ru.esstu.student.messaging.dialog_chat.entities.NewUserMessage
import ru.esstu.student.messaging.dialog_chat.entities.SentUserMessage
import ru.esstu.student.messaging.dialog_chat.util.toSentUserMessage
import ru.esstu.student.messaging.dialog_chat_new.datasources.di.dialogChatModuleNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.repo.IDialogChatRepositoryNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.repo.IDialogChatUpdateRepositoryNew
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.Sender


data class DialogChatState(
    val isOpponentLoading: Boolean = false,
    //val appUser: User? = null,
    val opponent: Sender? = null,

    val pageSize: Int = 20,
    val pages: List<Message> = emptyList(),
    val isPageLoading: Boolean = false,

    val isEndReached: Boolean = false,
    val error: ResponseError? = null,


    val message: NewUserMessage = NewUserMessage(),
    //val text: String = "",
    //val attachments: List<CachedFile> = emptyList(),

    //только-что отправленные сообщения
    val sentMessages: List<SentUserMessage> = emptyList()
)

sealed class DialogChatEvents {
    data class PassOpponent(val id: String) : DialogChatEvents()
    object CancelObserver : DialogChatEvents()
    object NextPage : DialogChatEvents()

    data class PassMessage(val message: String) : DialogChatEvents()
    data class PassReplyMessage(val message: Message) : DialogChatEvents()
    data class PassAttachments(val attachments: List<CachedFile>) : DialogChatEvents()
    data class RemoveAttachment(val attachment: CachedFile) : DialogChatEvents()
    object RemoveReplyMessage : DialogChatEvents()

    object SendMessage : DialogChatEvents()
    data class ResendMessage(val message: SentUserMessage) : DialogChatEvents()

    data class DownloadAttachment(val messageId: Long, val attachment: MessageAttachment) :
        DialogChatEvents()

    data class UpdateAttachment(val messageId: Long, val attachment: MessageAttachment) :
        DialogChatEvents()
}


class DialogChatViewModel constructor(
    private val dialogChatRepository: IDialogChatRepositoryNew = ESSTUSdk.dialogChatModuleNew.repo,
    private val dialogChatUpdateRepository: IDialogChatUpdateRepositoryNew = ESSTUSdk.dialogChatModuleNew.update,
) : ViewModel() {

    var dialogChatState by mutableStateOf(DialogChatState())
        private set

    fun onEvent(event: DialogChatEvents) {

        when (event) {
            is DialogChatEvents.PassOpponent -> viewModelScope.launch { onPassOpponent(event.id) }
            is DialogChatEvents.CancelObserver -> onCancelObserver()
            is DialogChatEvents.NextPage -> viewModelScope.launch { paginator.loadNext() }

            is DialogChatEvents.PassMessage -> withCachedMsg {
                dialogChatState =
                    dialogChatState.copy(message = dialogChatState.message.copy(text = event.message))
            }
            is DialogChatEvents.PassReplyMessage -> withCachedMsg {
                dialogChatState =
                    dialogChatState.copy(message = dialogChatState.message.copy(replyMessage = event.message))
            }
            is DialogChatEvents.PassAttachments -> withCachedMsg { onPassAttachments(event.attachments) }
            is DialogChatEvents.RemoveAttachment -> withCachedMsg { onRemoveAttachment(event.attachment) }
            is DialogChatEvents.RemoveReplyMessage -> withCachedMsg {
                dialogChatState =
                    dialogChatState.copy(message = dialogChatState.message.copy(replyMessage = null))
            }

            is DialogChatEvents.SendMessage -> withCachedMsg { onSendMessage() }
            is DialogChatEvents.ResendMessage -> viewModelScope.launch { onResendMessage(event.message) }

            is DialogChatEvents.DownloadAttachment -> {
                //fileDownloadRepository.downloadFile(event.attachment)
            }
            is DialogChatEvents.UpdateAttachment -> viewModelScope.launch {
                onUpdateAttachment(
                    event.messageId,
                    event.attachment
                )
            }
        }
    }

    private val paginator: Paginator<Int, Message> = Paginator(
        initialKey = 0,

        onRequest = { key ->
            val opponent =
                dialogChatState.opponent ?: return@Paginator Response.Error(ResponseError())

            dialogChatRepository.getPage(opponent.id, dialogChatState.pageSize, key)
        },

        onRefresh = { page ->
            dialogChatState =
                dialogChatState.copy(pages = page, error = null, isEndReached = page.isEmpty())

            val opponent = dialogChatState.opponent ?: return@Paginator

            installObserver(
                opponent.id,
                page.lastOrNull { it.status == DeliveryStatus.DELIVERED }?.id
                    ?: page.firstOrNull()?.id ?: 0
            )

            attachErredMessages(opponent.id)

            dialogChatState =
                dialogChatState.copy(message = dialogChatRepository.getUserMessage(opponent.id))

            updatePreview(page.firstOrNull())
        },
        onNext = { _, page ->
            dialogChatState = dialogChatState.copy(
                pages = dialogChatState.pages + page,
                error = null,
                isEndReached = page.isEmpty()
            )
        },

        onLoad = { dialogChatState = dialogChatState.copy(isPageLoading = it) },
        getNextKey = { _, _ -> dialogChatState.pages.size },
        onError = { dialogChatState = dialogChatState.copy(error = it) }
    )

    private var updatesObserver: Job? = null

    private fun onCancelObserver() {
        if (updatesObserver?.isCancelled != true)
            updatesObserver?.cancel()

        dialogChatState = dialogChatState.copy(
            opponent = null,
            message = NewUserMessage(),
            sentMessages = emptyList(),
            pages = emptyList()
        )
    }

    private fun installObserver(dialogId: String, lastMessageId: Long) {
        if (updatesObserver?.isCancelled != true)
            updatesObserver?.cancel()

        val updatesFlow = dialogChatUpdateRepository
            .collectUpdates(dialogId = dialogId, lastMessageId = lastMessageId)
            .cancellable()

        updatesObserver = viewModelScope.launch {
            updatesFlow.collect { response ->
                when (response) {
                    is Response.Error -> dialogChatState =
                        dialogChatState.copy(error = response.error)
                    is Response.Success -> {
                        dialogChatState = dialogChatState.copy(error = null)
                        if (response.data.isEmpty()) return@collect

                        val updates = response.data
                        Log.e("updates", updates.toString())
                        dialogChatRepository.setMessages(dialogId, updates)
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

    private suspend fun updatePreview(message: Message?) {
        val opponent = dialogChatState.opponent ?: return
        val msg = message ?: return
         //dialogChatRepository.updateLastMessageOnPreview(dialogId = opponent.id, message = msg)
    }

    private suspend fun attachErredMessages(dialogId: String) {
       // dialogChatState =
          //  dialogChatState.copy(sentMessages = dialogChatRepository.getErredMessages(dialogId))

        dialogChatState.sentMessages.forEach { msg ->
            if (msg.status == DeliveryStatus.ERRED)
                onEvent(DialogChatEvents.ResendMessage(msg))
        }
    }

    private suspend fun onPassOpponent(id: String) {
        onCancelObserver()




        dialogChatRepository.getOpponent(id).collect { response ->
            when (response) {
                is FlowResponse.Error -> dialogChatState =
                    dialogChatState.copy(error = response.error)
                is FlowResponse.Loading -> dialogChatState =
                    dialogChatState.copy(isOpponentLoading = response.isLoading)
                is FlowResponse.Success -> {
                    val isOpponentUpdating = dialogChatState.opponent?.id == response.data.id

                    dialogChatState = dialogChatState.copy(opponent = response.data, error = null)

                    if (!isOpponentUpdating) paginator.refresh()
                }
            }
        }
    }

    private fun onPassAttachments(attachments: List<CachedFile>) {
        val newAttachments =
            (dialogChatState.message.attachments + attachments).distinctBy { cachedFile -> cachedFile.uri }

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

            val opponent = dialogChatState.opponent ?: return@launch
            val message = dialogChatState.message
            dialogChatRepository.updateUserMessage(dialogId = opponent.id, message = message)
        }
    }

    private val mutex = Mutex()

    private suspend fun onSendMessage() {

        val opponent = dialogChatState.opponent ?: return

        val sentUserMessage = dialogChatState.message.toSentUserMessage()

        dialogChatState = dialogChatState.copy(
            message = NewUserMessage(),
            sentMessages = listOf(sentUserMessage) + dialogChatState.sentMessages
        )

        val result = dialogChatRepository.sendMessage(
            dialogId = opponent.id,
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
                            //dialogChatRepository.setErredMessage(opponent.id, erred)
                            erred
                        } else
                            sent
                    })

                is Response.Success ->
                    dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                        if (sent == sentUserMessage) {
                            val success =
                                sent.copy(id = result.data, status = DeliveryStatus.DELIVERED)
                            //dialogChatRepository.delErredMessage(sent.id)
                            success
                        } else
                            sent
                    })
            }
        }
    }


    private suspend fun onResendMessage(message: SentUserMessage) {

        val opponent = dialogChatState.opponent ?: return

        val updatedMessage = message.copy(status = DeliveryStatus.SENT)

        dialogChatState =
            dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                if (sent == message)
                    updatedMessage
                else
                    sent
            })

        val result = dialogChatRepository.sendMessage(
            dialogId = opponent.id,
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
                            dialogChatRepository.setErredMessage(opponent.id, erred)
                            erred
                        } else
                            sent
                    })

                is Response.Success ->
                    dialogChatState.copy(sentMessages = dialogChatState.sentMessages.map { sent ->
                        if (sent == updatedMessage) {
                            val success =
                                sent.copy(id = result.data, status = DeliveryStatus.DELIVERED)
                            dialogChatRepository.delErredMessage(sent.id)
                            success
                        } else
                            sent
                    })
            }
        }
    }


    private suspend fun onUpdateAttachment(messageId: Long, attachment: MessageAttachment) {
        /* mutex.withLock {

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

         dialogChatRepository.updateFile(messageId, attachment)*/
    }
}