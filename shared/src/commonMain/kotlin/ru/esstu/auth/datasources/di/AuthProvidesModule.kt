package ru.esstu.auth.datasources.di


import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.auth.datasources.api.student_teacher.AuthApImpl
import ru.esstu.auth.datasources.api.student_teacher.AuthApi
import ru.esstu.auth.datasources.local.TokenDSManagerImpl
import ru.esstu.auth.datasources.repo.AuthRepositoryImpl
import ru.esstu.auth.datasources.repo.DataStore
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.datasources.repo.getSettings
import kotlin.native.concurrent.ThreadLocal

internal val authProvidesModule = DI.Module(
    name = "AuthProvidesModule",
    init = {


        bind<AuthApi>(tag = "AuthApi") with singleton {
            AuthApImpl(
                portalApi = instance()
            )
        }

        bind<IAuthRepository>() with singleton {
            AuthRepositoryImpl(
                portalApi = instance(tag = "AuthApi"),
                cache = instance<TokenDSManagerImpl>()
            )
        }

        bind<TokenDSManagerImpl>() with  singleton { TokenDSManagerImpl(
            authDataStore = getSettings().settings.create()
        ) }
    }
)

@ThreadLocal
object AuthModule {
    val authModule: IAuthRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.repoAuth: AuthModule
    get() = AuthModule