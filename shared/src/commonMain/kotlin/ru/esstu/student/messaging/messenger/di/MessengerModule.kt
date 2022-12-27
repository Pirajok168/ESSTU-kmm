package ru.esstu.student.messaging.messenger.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.student.domain.db.DatabaseStudent
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.messaging.messenger.datasources.db.timestamp.TimestampDao
import ru.esstu.student.messaging.messenger.datasources.db.timestamp.TimestampDatabase

internal val messengerModule  = DI.Module("MessengerModule"){
    bind<TimestampDao>() with singleton {
        TimestampDatabase(
            database = instance<IDatabaseStudent>().getDataBase()
        )
    }

}