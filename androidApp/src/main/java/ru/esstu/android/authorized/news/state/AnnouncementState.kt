package ru.esstu.android.authorized.news.state

import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.features.news.announcement.domain.model.NewsNode



data class AnnouncementState(

    val pages: List<NewsNode> = emptyList(),
    val isPagesLoading: Boolean = false,
    val pageLoadingError: ResponseError? = null,
    val isEndReached:Boolean = false,

    val pageSize: Int = 10
)