package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities




class UserCachedFileEntity(

    val appUserId:String,
    val dialogId:String,


    val source: String,
    val name: String,
    val ext: String,
    val size: Long,
    val type: String
) {

    var id: Int? = null
}