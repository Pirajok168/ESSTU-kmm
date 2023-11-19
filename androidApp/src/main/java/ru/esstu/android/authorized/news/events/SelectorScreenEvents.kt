package ru.esstu.android.authorized.news.events

import ru.esstu.student.news.entities.NewsNode

sealed class SelectorScreenEvents {
    data class PassNode(val node: NewsNode) : SelectorScreenEvents()
    
}