package ru.esstu.android.domain.datasources.download_worker.entities

data class LoadingFile(
    val id: Int,
    val type: String,
    val name: String,
    val ext: String,
    val uri: String,
    val size: Long
)