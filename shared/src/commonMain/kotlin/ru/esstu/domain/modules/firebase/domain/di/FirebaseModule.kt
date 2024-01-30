package ru.esstu.domain.modules.firebase.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.esstu.ESSTUSdk
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.domain.modules.firebase.data.api.FirebaseApiImpl
import ru.esstu.domain.modules.firebase.data.api.IFirebaseApi
import ru.esstu.domain.modules.firebase.domain.repo.FirebaseRepositoryImpl
import ru.esstu.domain.modules.firebase.domain.repo.IFirebaseRepository
import ru.esstu.student.profile.porfolio.data.api.PortfolioApi
import ru.esstu.student.profile.porfolio.data.api.PortfolioApiImpl
import ru.esstu.student.profile.porfolio.domain.repository.IPortfolioRepository
import ru.esstu.student.profile.porfolio.domain.repository.PortfolioRepositoryImpl
import kotlin.native.concurrent.ThreadLocal

internal val firebaseModule = DI.Module("firebaseModule"){
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
object FirebaseModule{
    val repo: IFirebaseRepository
        get() = ESSTUSdk.di.instance()

}


val ESSTUSdk.firebaseModule: FirebaseModule
    get() = FirebaseModule