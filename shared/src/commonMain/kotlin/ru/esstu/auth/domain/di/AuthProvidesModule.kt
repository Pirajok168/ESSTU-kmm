package ru.esstu.auth.domain.di


import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.auth.data.api.di.authorizationDataModule
import ru.esstu.auth.domain.repo.AuthRepositoryImpl
import ru.esstu.auth.domain.repo.IAuthRepository
import ru.esstu.data.token.repository.ILoginDataRepository
import ru.esstu.data.token.repository.LoginDataRepositoryImpl
import kotlin.native.concurrent.ThreadLocal


fun authorizationDomainModule() = DI.Module("authorizationDomainModule") {
    importOnce(authorizationDataModule())

    bind<IAuthRepository>() with singleton {
        AuthRepositoryImpl(
            portalApi = instance(),
            cache = instance<LoginDataRepositoryImpl>()
        )
    }
}

@ThreadLocal
object AuthModule {
    val authModule: IAuthRepository
        get() = ESSTUSdk.di.instance()

    val tokenDSManagerImpl: ILoginDataRepository
        get() = ESSTUSdk.di.instance()
}

val ESSTUSdk.repoAuth: AuthModule
    get() = AuthModule