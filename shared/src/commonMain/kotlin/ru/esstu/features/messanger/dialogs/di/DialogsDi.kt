package ru.esstu.features.messanger.dialogs.di

import org.kodein.di.DI
import ru.esstu.di.sharedDi
import ru.esstu.features.messanger.dialogs.domain.di.dialogsDomainModule


fun dialogsDi() = DI {
    extend(sharedDi)
    importOnce(dialogsDomainModule())
}