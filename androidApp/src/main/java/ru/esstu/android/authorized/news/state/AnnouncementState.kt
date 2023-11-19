package ru.esstu.android.authorized.news.state

import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.news.entities.NewsNode



data class AnnouncementState(

    val pages: List<NewsNode> = emptyList(),
    val isPagesLoading: Boolean = false,
    val pageLoadingError: ResponseError? = null,
    val isEndReached:Boolean = false,

    val pageSize: Int = 10
)