package ru.esstu.student.profile.student.main_profile.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatRepository
import ru.esstu.student.messaging.group_chat.datasources.repo.IGroupChatUpdateRepository
import ru.esstu.student.profile.student.main_profile.data.api.ProfileApi
import ru.esstu.student.profile.student.main_profile.data.api.ProfileApiImpl
import ru.esstu.student.profile.student.main_profile.domain.repository.IProfileRepository
import ru.esstu.student.profile.student.main_profile.domain.repository.ProfileRepositoryImpl
import kotlin.native.concurrent.ThreadLocal


internal val profileDIModule = DI.Module("ProfileDI"){
    bind<ProfileApi>() with singleton {
        ProfileApiImpl(
            portalApi = instance()
        )
    }

    bind<IProfileRepository>() with singleton {
        ProfileRepositoryImpl(
            api = instance(),
            auth = instance()
        )
    }
}

@ThreadLocal
object ProfileDI {
    val repo: IProfileRepository
        get() = ESSTUSdk.di.instance()

}

val ESSTUSdk.profileDIModule: ProfileDI
    get() = ProfileDI