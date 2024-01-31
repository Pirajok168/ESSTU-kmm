package ru.esstu.features.firebase.di

import org.kodein.di.DI
import ru.esstu.features.firebase.domain.di.firebaseDomainModule


internal val featureFirebaseModule = DI.Module("FeatureFirebaseModule") {
    importAll(
        firebaseDomainModule()
    )
}