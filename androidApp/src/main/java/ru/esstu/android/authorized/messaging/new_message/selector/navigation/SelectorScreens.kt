package ru.esstu.android.authorized.messaging.new_message.selector.navigation

import ru.esstu.android.domain.navigation.Route

sealed class SelectorScreens : Route<Unit>() {
    object Root : SelectorScreens()
    object Selector : SelectorScreens()
}