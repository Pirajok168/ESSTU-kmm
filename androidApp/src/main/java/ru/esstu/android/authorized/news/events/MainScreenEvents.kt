package ru.esstu.android.authorized.news.events

import ru.esstu.android.authorized.news.component.FilterNews

sealed class MainScreenEvents{
    data class ChooseOtherFilter(val category: FilterNews): MainScreenEvents()
}
