package ru.esstu.android.student.messaging.new_message.new_support.navigation

import ru.esstu.android.domain.navigation.Route

sealed class NewSupportScreens : Route<Unit>() {
    object Root : NewSupportScreens()
    object NewSupportScreen : NewSupportScreens()
    object NewSupportThemeSelectorScreen : NewSupportScreens()
}
