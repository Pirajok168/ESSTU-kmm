package ru.esstu

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform