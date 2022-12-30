package ru.esstu

import com.russhwolf.settings.Settings
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ktor.student_teacher.AuthApImpl
import ru.esstu.local.TokenDSManagerImpl
import ru.esstu.student_teacher.AuthApi
import kotlin.native.concurrent.ThreadLocal


val repoProvidesModule = DI.Module(
    name = "RepoProvidesModule",
    init = {

        bind<AuthApi>() with singleton {
            AuthApImpl(
                portalApi = instance()
            )
        }
        bind<ITokenDSManager>() with  singleton { TokenDSManagerImpl(
            authDataStore = Settings()
        ) }

        bind<IAuthRepository>() with singleton {
            AuthRepositoryImpl(
                portalApi = instance(),
                cache = instance<ITokenDSManager>()
            )
        }

    }
)

