package ru.esstu.android.authorized.messaging.dialog_chat.navigation

import androidx.navigation.NavType
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatArguments
import ru.esstu.android.domain.navigation.Route

enum class DialogChatArguments {
    OPPONENT_ID
}

object DialogChatScreen : Route<DialogChatArguments>(
    navArgs = mapOf(DialogChatArguments.OPPONENT_ID to NavType.StringType)
) {
    fun navigate(opponentId: String) = navigate {
        when (it) {
            DialogChatArguments.OPPONENT_ID -> opponentId
        }
    }
}