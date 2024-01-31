package ru.esstu.android.authorized.news.events

import ru.esstu.android.authorized.news.component.FilterNews
import ru.esstu.features.news.announcement.domain.model.NewsNode

sealed class SelectorScreenEvents {
    data class PassNode(val node: NewsNode, val selectedFilterNews: FilterNews) : SelectorScreenEvents()

}