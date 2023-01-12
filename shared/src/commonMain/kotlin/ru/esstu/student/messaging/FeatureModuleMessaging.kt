package ru.esstu.student.messaging

import org.kodein.di.DI
import ru.esstu.student.messaging.dialog_chat.datasources.di.dialogChatModuleNew
import ru.esstu.student.messaging.group_chat.di.groupChatModuleNew
import ru.esstu.student.messaging.messenger.conversations.di.conversationModule
import ru.esstu.student.messaging.messenger.di.messengerModule
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew


internal val featureModuleMessaging = DI.Module("featureModuleMessaging") {
    importAll(
        messengerModule,
        dialogsModuleNew,
        dialogChatModuleNew,
        conversationModule,
        groupChatModuleNew
        )

}