package ru.esstu.features.news.announcement.data.db


import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.esstu.features.news.announcement.data.db.entities.NewsAttachmentEntity
import ru.esstu.features.news.announcement.data.db.entities.NewsEntity
import ru.esstu.features.news.announcement.data.db.entities.UserEntity
import ru.esstu.features.news.announcement.data.db.entities.relations.NewsWithAttachments
import ru.esstu.student.EsstuDatabase

internal class Database(database: EsstuDatabase) : NewsDao {

    private val adapter = object : ColumnAdapter<UserEntity, String> {

        override fun decode(databaseValue: String): UserEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: UserEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val listAdapter = object : ColumnAdapter<List<NewsAttachmentEntity>, String> {
        override fun decode(databaseValue: String): List<NewsAttachmentEntity> {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: List<NewsAttachmentEntity>): String {
            return Json {}.encodeToString(value)
        }

    }

    private val dbQuery = database.newsDatabaseQueries


    override suspend fun getAllNewsWithAttachments(): List<NewsWithAttachments> {
        return dbQuery.getAllNewsWithAttachments(::map).executeAsList()
    }

    override suspend fun getNewsWithAttachments(
        limit: Int,
        offset: Int
    ): List<NewsWithAttachments> {
        return dbQuery.getNewsWithAttachments(limit.toLong(), offset.toLong(), ::map)
            .executeAsList()
    }

    override suspend fun setNewsWithAttachments(news: List<NewsWithAttachments>) {
        news.forEach { newsNode ->
            dbQuery.setNewsWithAttachments(
                id = newsNode.news.id,
                fromUser = newsNode.news.from,
                date = newsNode.news.date,
                title = newsNode.news.title,
                message = newsNode.news.message,
                listAttachments = newsNode.attachments
            )
        }
    }

    override suspend fun clearAll() {
        dbQuery.removeAllNews()
    }
}

private fun map(
    id: Long,
    fromUser: UserEntity,
    date: Long,
    title: String,
    message: String,
    listAttachments: List<NewsAttachmentEntity>?
): NewsWithAttachments {
    return NewsWithAttachments(
        news = NewsEntity(
            id,
            fromUser,
            date,
            title,
            message,
        ),
        attachments = listAttachments ?: emptyList()
    )
}
