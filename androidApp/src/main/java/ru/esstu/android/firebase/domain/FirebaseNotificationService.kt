package ru.esstu.android.firebase.domain

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatArguments
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatArguments
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.firebase.domain.model.MessageType
import ru.esstu.android.firebase.domain.model.determine
import ru.esstu.android.notification.DefaultNotificationManager
import ru.esstu.android.student.navigation.StudentRoutes
import ru.esstu.domain.modules.firebase.domain.di.firebaseModule
import ru.esstu.domain.modules.firebase.domain.repo.IFirebaseRepository

class FirebaseNotificationService: FirebaseMessagingService()  {
    private val firebaseRepo: IFirebaseRepository = ESSTUSdk.firebaseModule.repo
    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            firebaseRepo.registerFirebaseToken(token)
        }
        super.onNewToken(token)
    }

    override fun handleIntent(intent: Intent?) {
        val message = RemoteMessage(intent?.extras)

        val typedMessage = message.determine()
        val notificationManager = DefaultNotificationManager()

        if (typedMessage == null) {
            notificationManager.pushNotification(
                applicationContext,
                message.messageId.hashCode(),
                message.notification?.title.orEmpty(),
                message.notification?.body.orEmpty()
            )
            return
        }



        when (typedMessage) {
            is MessageType.Dialogue -> {
                notificationManager.pushNotification(
                    context = applicationContext,
                    body = typedMessage.message.body,
                    title = typedMessage.message.title.orEmpty(),
                    id = typedMessage.message.id.hashCode(),
                    deepLink = DialogChatScreen.navigateDeepLink {
                        when (it) {
                            DialogChatArguments.OPPONENT_ID -> typedMessage.message.from
                        }
                    }
                )

            }
            is MessageType.Chat -> {
                notificationManager.pushNotification(
                    context = applicationContext,
                    body = typedMessage.message.body,
                    title = typedMessage.message.title.orEmpty(),
                    id = typedMessage.message.id.hashCode(),
                    deepLink =  GroupChatScreen.navigateDeepLink {
                        when (it) {
                            GroupChatArguments.GROUP_ID -> typedMessage.message.parentId
                        }
                    }
                )

            }
            is MessageType.Support -> {
                notificationManager.pushNotification(
                    context = applicationContext,
                    body = typedMessage.message.body,
                    title = typedMessage.message.title.orEmpty(),
                    id = typedMessage.message.id.hashCode(),
                    deepLink = GroupChatScreen.navigateDeepLink {
                        when (it) {
                            GroupChatArguments.GROUP_ID -> typedMessage.message.parentId
                        }
                    }
                )

            }
            is MessageType.Announcement -> {
                notificationManager.pushNotification(
                    context = applicationContext,
                    body = typedMessage.message.body,
                    title = typedMessage.message.title.orEmpty(),
                    id = typedMessage.message.id.hashCode(),
                    deepLink = StudentRoutes.BottomNavScreen.navigateDeepLink()
                )

            }

            is MessageType.AdmissionStatus -> {
                notificationManager.pushNotification(
                    applicationContext,
                    message.messageId.hashCode(),
                    typedMessage.message.title.orEmpty()
                )
            }
        }
        super.handleIntent(intent)
    }
}