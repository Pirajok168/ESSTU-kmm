package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities




data class UserCachedFileEntity(

    val appUserId:String,
    val dialogId:String,

    var id: Long? = null,
    val source: String,
    val name: String,
    val ext: String,
    val size: Long,
    val type: String
) {


}