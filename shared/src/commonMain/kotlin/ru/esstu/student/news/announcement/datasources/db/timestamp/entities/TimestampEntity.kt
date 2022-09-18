package ru.esstu.student.news.announcement.datasources.db.timestamp.entities

import kotlinx.serialization.Serializable


@Serializable
data class TimestampEntity(
    val appUserId: String,
    val timestamp: Long
)