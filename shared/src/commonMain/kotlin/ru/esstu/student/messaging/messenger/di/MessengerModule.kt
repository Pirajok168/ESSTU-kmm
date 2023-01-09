package ru.esstu.student.messaging.messenger.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.domain.db.DatabaseStudent
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.datasources.db.timestamp.TimestampDao
import ru.esstu.student.messaging.messenger.datasources.db.timestamp.TimestampDatabase
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.DialogsUpdatesRepositoryImpl
import ru.esstu.student.messaging.messenger.dialogs.datasources.repo.IDialogsUpdatesRepository
import kotlin.native.concurrent.ThreadLocal

internal val messengerModule  = DI.Module("MessengerModule"){
    bind<TimestampDao>() with singleton {
        TimestampDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

    bind<IDialogsUpdatesRepository>() with singleton {
        DialogsUpdatesRepositoryImpl(
            instance(),
            instance(),
            instance()
        )
    }
}

@ThreadLocal
object MessengerModule {
    val update: IDialogsUpdatesRepository
        get() = ESSTUSdk.di.instance()



}

val ESSTUSdk.messengerModule: MessengerModule
    get() = MessengerModule