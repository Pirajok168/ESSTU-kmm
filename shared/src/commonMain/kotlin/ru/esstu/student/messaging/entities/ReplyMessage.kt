package ru.esstu.student.messaging.entities

import com.soywiz.klock.DateTime


data class ReplyMessage(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val attachmentsCount: Int
){
    val dateTim: DateTime = DateTime(date)
}