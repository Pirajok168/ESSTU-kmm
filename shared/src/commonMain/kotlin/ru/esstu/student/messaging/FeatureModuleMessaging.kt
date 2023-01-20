package ru.esstu.student.messaging

import org.kodein.di.DI
import ru.esstu.student.messaging.dialog_chat.datasources.di.dialogChatModuleNew
import ru.esstu.student.messaging.group_chat.di.groupChatModuleNew
import ru.esstu.student.messaging.messenger.appeals.di.appealsModule
import ru.esstu.student.messaging.messenger.conversations.di.conversationModule
import ru.esstu.student.messaging.messenger.di.messengerModule
import ru.esstu.student.messaging.messenger.dialogs.di.dialogsModuleNew
import ru.esstu.student.messaging.messenger.new_message.new_dialog.di.newDialogModule
import ru.esstu.student.messaging.messenger.new_message.new_support.di.newSupportModule
import ru.esstu.student.messaging.messenger.supports.di.supportsModule


internal val featureModuleMessaging = DI.Module("featureModuleMessaging") {
    importAll(
        messengerModule,
        dialogsModuleNew,
        dialogChatModuleNew,
        conversationModule,
        appealsModule,
        supportsModule,
        groupChatModuleNew,
        newDialogModule,
        newSupportModule,
    )

}