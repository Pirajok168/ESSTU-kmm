package ru.esstu.android.student.messaging.group_chat.navigation

import androidx.navigation.NavType
import ru.esstu.android.domain.navigation.Route

enum class GroupChatArguments {
    GROUP_ID
}

object GroupChatScreen : Route<GroupChatArguments>(
    navArgs = mapOf(GroupChatArguments.GROUP_ID to NavType.StringType)
){
    fun navigate(groupId: Int) = navigate {
        when (it) {
            GroupChatArguments.GROUP_ID -> groupId
        }
    }
}