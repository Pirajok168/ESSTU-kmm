package ru.esstu.auth.datasources.di


import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.auth.datasources.api.student_teacher.AuthApImpl
import ru.esstu.auth.datasources.api.student_teacher.AuthApi
import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.local.LoginDataRepositoryImpl
import ru.esstu.auth.datasources.repo.AuthRepositoryImpl
import ru.esstu.auth.datasources.repo.IAuthRepository
import kotlin.native.concurrent.ThreadLocal

internal val authProvidesModule = DI.Module(
    name = "AuthProvidesModule",
    init = {


        bind<AuthApi>() with singleton {
            AuthApImpl(
                unauthorizedApi = instance()
            )
        }

        bind<IAuthRepository>() with singleton {
            AuthRepositoryImpl(
                portalApi = instance(),
                cache = instance<LoginDataRepositoryImpl>()
            )
        }

    }
)

@ThreadLocal
object AuthModule {
    val authModule: IAuthRepository
        get() = ESSTUSdk.di.instance()

    val tokenDSManagerImpl: ILoginDataRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.repoAuth: AuthModule
    get() = AuthModule