package ru.esstu.student.profile.student.porfolio.achivement.domain.model

import ru.esstu.student.profile.student.porfolio.achivement.domain.ASIC_STORAGE_DEFAULT_PATH
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import kotlin.random.Random

data class Attachment(
    val id: Int,
    val fileUri: String,

    val loadProgress: Float? = null,
    val localFileUri: String?,

    val name: String?,
    val ext: String? = "",
    val size: Int,
    val type: String?
) {
    val isImage: Boolean get() = type?.contains("image", true) == true
    val closestUri: String get() = localFileUri ?: fileUri
    val nameWithExt: String? get() = name?.let { "$it${ext?.let { ex -> ".$ex" }}" }
}

suspend fun PortfolioFileRequestResponse.toAttachment(): Attachment =
    Attachment(
        id = id ?: Random.nextInt() ,
        fileUri = "$ASIC_STORAGE_DEFAULT_PATH$fileCode",
        ext = eventName.orEmpty().split('.').let { if (it.size > 1) it.last() else "" },
        size = 0,
        name = eventName.orEmpty(),
        localFileUri = null,
        type = ""
    )