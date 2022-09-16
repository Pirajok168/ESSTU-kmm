package ru.esstu

import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.direct
import ru.esstu.domain.featureModuleDomain

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ESSTUSdk {
    internal val di: DirectDI
        get() = requireNotNull(_di)
    private var _di: DirectDI? = null

    init {
        if (_di != null) {
            _di = null
        }

        val direct = DI {
            importAll(
                featureModuleDomain
            )
        }.direct

        _di = direct
    }
}