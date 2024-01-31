package ru.esstu.auth.data.api.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.auth.data.api.student_teacher.AuthApImpl
import ru.esstu.auth.data.api.student_teacher.AuthApi

fun authorizationDataModule() = DI.Module("authorizationDataModule") {
    bind<AuthApi>() with singleton {
        AuthApImpl(
            unauthorizedApi = instance()
        )
    }
}