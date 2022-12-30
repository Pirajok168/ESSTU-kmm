package ru.esstu

import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.native.concurrent.ThreadLocal


@ThreadLocal
object ESSTUSdk2 {
    internal val di: DirectDI
        get() = requireNotNull(_di)
    private var _di: DirectDI? = null


    init {
        if (_di != null) {
            _di = null
        }

        val direct = DI {
            importAll(
                coreModule,
                repoProvidesModule,
            )
        }.direct

        _di = direct
    }
}

@ThreadLocal
object AuthModule {
    val authModule: IAuthRepository
        get() = ESSTUSdk2.di.instance()

    val tokenDSManagerImpl: ITokenDSManager
        get() = ESSTUSdk2.di.instance()
}

val ESSTUSdk2.repoAuth: AuthModule
    get() = AuthModule
