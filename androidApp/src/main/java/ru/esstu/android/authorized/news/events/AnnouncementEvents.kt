package ru.esstu.android.authorized.news.events

sealed class AnnouncementEvents {
    data object LoadAndRefresh:AnnouncementEvents()
    data object LoadNext : AnnouncementEvents()
    data object Refresh : AnnouncementEvents()
}