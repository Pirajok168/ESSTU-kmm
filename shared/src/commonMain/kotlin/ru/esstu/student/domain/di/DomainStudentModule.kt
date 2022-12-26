package ru.esstu.student.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import ru.esstu.student.domain.db.DatabaseStudent
import ru.esstu.student.domain.db.IDatabaseStudent
import ru.esstu.student.domain.db.databaseFactory


internal val domainStudentModule = DI.Module("domainStudentModule"){
    bind<IDatabaseStudent>() with singleton {
        val driverFactory = databaseFactory()
        DatabaseStudent(
            sqlDriver = driverFactory.sqlDriver
        )
    }

}