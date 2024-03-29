package ru.esstu.android.authorized.news.state

import ru.esstu.android.authorized.news.component.FilterNews
import ru.esstu.student.news.entities.NewsNode

data class SelectorScreenState(
    val node: NewsNode? = null,
    val selected: FilterNews? = null
)
