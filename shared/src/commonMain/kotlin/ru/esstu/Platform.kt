package ru.esstu

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


expect fun debugBuild()