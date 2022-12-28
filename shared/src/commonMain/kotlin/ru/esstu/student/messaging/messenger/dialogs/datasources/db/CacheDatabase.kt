package ru.esstu.student.messaging.messenger.dialogs.datasources.db

import com.squareup.sqldelight.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.DialogEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.datasources.toReplyMessageEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUserEntity
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

class CacheDatabase(
    database: EsstuDatabase,
    coroutineScope: CoroutineScope
): CacheDao {
    private val dbQueries = database.dialogsTableNewQueries
    override val cachedDialogs: MutableSharedFlow<List<Dialog>> = MutableSharedFlow()

    init {
        dbQueries.getAllDialogs().addListener(object : Query.Listener{
            override fun queryResultsChanged() {
                coroutineScope.launch {
                    val dialogs = getDialogWithLastMessage("", 10, 0).map {  it.toDialogs() }
                    cachedDialogs.emit(dialogs)
                }

            }
        })
    }



    override suspend fun setLastMessage(message: Message) {
        message.apply {
            dbQueries.setLastMessage(id, from.toUserEntity(),date,message.message,status.name,replyMessage?.toReplyMessageEntity(), attachments)
        }
    }

    override suspend fun setDialog(appUserId: String, dialog: ru.esstu.student.messaging.messenger.dialogs.entities.Dialog) {
        dialog.apply {
            dbQueries.setDialog(id,appUserId, lastMessage?.id, opponent.toUserEntity(), unreadMessageCount, notifyAboutIt)
        }
    }

    override suspend fun getDialogWithLastMessage(
        appUserId: String,
        pageSize: Int,
        pageOffset: Int
    ): List<DialogWithMessage> {
        fun map(
            idDialog: String,
            appUserId: String,
            lastMessageId: Long?,
            opponent: UserEntity,
            unread: Int,
            notifyAboutIt: Boolean,
            messageId: Long?,
            fromUser: UserEntity?,
            date: Long?,
            message: String?,
            status: String?,
            replyMessage: ReplyMessageEntity?,
            countAttachments: Int?
        ): DialogWithMessage{
            return DialogWithMessage(
                dialog = DialogEntity(
                    appUserId,
                    idDialog,
                    0,
                    lastMessageId,
                    opponent,
                    unread,
                    notifyAboutIt
                ),
                lastMessage = MessageWithAttachments(
                    message = MessageEntity(
                        messageId!!,
                        fromUser!!,
                        date!!,
                        message,
                        status!!,
                        replyMessage,
                    ),
                    attachments = countAttachments!!
                )
            )
        }
       return dbQueries.getDialogsWithLastMessage(pageSize.toLong(), pageOffset.toLong(), ::map).executeAsList()
    }


}