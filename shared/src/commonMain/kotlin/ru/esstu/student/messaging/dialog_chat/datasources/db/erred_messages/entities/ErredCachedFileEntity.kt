package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities


class ErredCachedFileEntity(

    val messageId: Long,


    val source: ByteArray,
    val name: String,
    val ext: String,
    val size: Long,
    val type: String
) {

    var id: Int? = null
}