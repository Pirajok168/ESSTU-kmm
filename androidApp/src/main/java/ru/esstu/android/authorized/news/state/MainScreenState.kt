package ru.esstu.android.authorized.news.state

import ru.esstu.android.R
import ru.esstu.android.authorized.news.component.FilterNews

private val list = listOf( FilterNews(titleId = R.string.announcement))

data class MainScreenState(
    val filterNews: List<FilterNews> = list,
    val selected: FilterNews = list.first()
)
