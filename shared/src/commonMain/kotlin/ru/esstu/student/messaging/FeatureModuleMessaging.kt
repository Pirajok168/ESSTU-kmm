package ru.esstu.student.messaging

import org.kodein.di.DI
import ru.esstu.student.messaging.dialog_chat.datasources.di.dialogChatModule
import ru.esstu.student.messaging.group_chat.datasources.di.groupChatModule

internal val featureModuleMessaging = DI.Module("featureModuleMessaging") {
    importAll(
        dialogChatModule,
        groupChatModule
    )

}