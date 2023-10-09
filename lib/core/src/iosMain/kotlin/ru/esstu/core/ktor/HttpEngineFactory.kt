package ru.esstu.core.ktor

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual class HttpEngineFactory  actual constructor() {
    actual fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig> {
        return Darwin
    }
}