package ru.esstu.domain.modules.firebase.di

import org.kodein.di.DI
import ru.esstu.domain.modules.firebase.domain.di.firebaseModule
import ru.esstu.student.profile.porfolio.domain.di.portfolioModule


internal val featureFirebaseModule = DI.Module("FeatureFirebaseModule") {
    importAll(
        firebaseModule
    )
}