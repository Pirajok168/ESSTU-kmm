package ru.esstu.android.student.messaging.new_message.new_appeal.navigation

import ru.esstu.android.domain.navigation.Route

sealed class NewAppealsScreens : Route<Unit>() {
    object Root:NewAppealsScreens()
    object NewAppealsScreen:NewAppealsScreens()
    object NewAppealsDepartmentSelectionScreen:NewAppealsScreens()
    object NewAppealsThemeSelectionScreen:NewAppealsScreens()
}
