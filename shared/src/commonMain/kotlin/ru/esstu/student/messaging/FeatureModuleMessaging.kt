package ru.esstu.student.messaging

import org.kodein.di.DI
import ru.esstu.student.messaging.dialog_chat.datasources.di.dialogChatModule
import ru.esstu.student.messaging.group_chat.datasources.di.groupChatModule
import ru.esstu.student.messaging.messenger.appeals.di.appealsModule
import ru.esstu.student.messaging.messenger.conversations.di.conversationModule
import ru.esstu.student.messaging.messenger.di.messengerModule
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModule
import ru.esstu.student.messaging.messenger.supports.di.supportModule

internal val featureModuleMessaging = DI.Module("featureModuleMessaging") {
    importAll(
        messengerModule,
        dialogChatModule,
        groupChatModule,
        appealsModule,
        conversationModule,
        supportModule,
        dialogsModule
    )

}