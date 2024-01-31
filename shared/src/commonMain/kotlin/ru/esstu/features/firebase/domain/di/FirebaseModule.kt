package ru.esstu.features.firebase.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.features.firebase.data.api.FirebaseApiImpl
import ru.esstu.features.firebase.data.api.IFirebaseApi
import ru.esstu.features.firebase.domain.repo.FirebaseRepositoryImpl
import ru.esstu.features.firebase.domain.repo.IFirebaseRepository
import kotlin.native.concurrent.ThreadLocal


fun firebaseDomainModule() = DI.Module("firebaseDomainModule") {
    bind<IFirebaseApi>() with singleton {
        FirebaseApiImpl(
            instance()
        )
    }

    bind<IFirebaseRepository>() with singleton {
        FirebaseRepositoryImpl(
            api = instance()
        )
    }
}


@ThreadLocal
object FirebaseModule {
    val repo: IFirebaseRepository
        get() = ESSTUSdk.di.instance()

}


val ESSTUSdk.firebaseModule: FirebaseModule
    get() = FirebaseModule