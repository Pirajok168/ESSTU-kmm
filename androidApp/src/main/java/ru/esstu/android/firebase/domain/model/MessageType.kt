package ru.esstu.android.firebase.domain.model

import com.google.firebase.messaging.RemoteMessage
import ru.esstu.android.firebase.domain.model.admission_status.AdmissionStatus
import ru.esstu.android.firebase.domain.model.admission_status.FirebaseAdmissionStatusResponse
import ru.esstu.android.firebase.domain.model.message.FirebaseConversationResponse
import ru.esstu.android.firebase.domain.model.message.FirebaseDialogResponse
import ru.esstu.android.firebase.domain.model.news.FirebaseNewsResponse
import java.util.Date

sealed class MessageType {
    data class Dialogue(val message: FirebaseDialogResponse) : MessageType()
    data class Chat(val message: FirebaseConversationResponse) : MessageType()
    data class Support(val message: FirebaseConversationResponse) : MessageType()
    data class Announcement(val message: FirebaseNewsResponse) : MessageType()
    data class AdmissionStatus(val message: FirebaseAdmissionStatusResponse) : MessageType()
}

fun RemoteMessage.determine(): MessageType? {
    return when (data["tag"]) {
        "DIALOGUE" ->
            MessageType.Dialogue(
                FirebaseDialogResponse(
                    data["from_id"] ?: return null,
                    data["message_id"].orEmpty(),
                    data["title"],
                    data["text"].orEmpty(),
                    Date(data["order"]?.toLongOrNull() ?: sentTime),
                    data["from_id"] ?: return null,
                    data["attachments"]?.toIntOrNull()?:0
                )
            )

        "CHAT" ->
            MessageType.Chat(
                FirebaseConversationResponse(
                    data["chat_id"] ?: return null,
                    data["message_id"].orEmpty(),
                    data["title"],
                    data["text"].orEmpty(),
                    Date(data["order"]?.toLongOrNull() ?:sentTime),
                    data["chat_id"] ?: return null,
                    data["attachments"]?.toIntOrNull()?:0,
                    data["from_id"].orEmpty()
                )
            )

        "SUPPORT" ->
            MessageType.Support(
                FirebaseConversationResponse(
                    data["chat_id"] ?: return null,
                    data["message_id"].orEmpty(),
                    data["title"],
                    data["text"].orEmpty(),
                    Date(data["order"]?.toLongOrNull() ?: sentTime),
                    data["chat_id"] ?: return null,
                    data["attachments"]?.toIntOrNull()?:0,
                    data["from_id"].orEmpty()
                )
            )

        "ANNOUNCEMENT" -> MessageType.Announcement(
            FirebaseNewsResponse(
                messageId.orEmpty(),
                data["title"],
                data["text"].orEmpty(),
                Date(sentTime),
            )
        )

        "STATUS_APPLICATION" -> MessageType.AdmissionStatus(
            FirebaseAdmissionStatusResponse(
                messageId.orEmpty(),
                data["title"],
                Date(sentTime),
                //todo попросить добавить отправку ствтуса в нормальном виде и получать по AdmissionStatus.byConstName()
                when (data["title"]) {
                    "Ваше заявление в процессе заполнения" -> AdmissionStatus.PROCESS_FILLING
                    "Ваше заявление отправлено на рассмотрение" -> AdmissionStatus.SENT
                    "Ошибка при импорте заявления" -> AdmissionStatus.FAIL
                    "Ваше заявление принято" -> AdmissionStatus.ACCEPTED
                    "Ваше заявление отклонено" -> AdmissionStatus.REJECTED
                    else -> return null
                }
            )
        )
        else -> null
    }
}