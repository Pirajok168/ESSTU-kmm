package ru.esstu.student.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("postCategoryCode")
    val postCategoryCode: String?,
    @SerialName("postCategoryName")
    val postCategoryName: String?,
    @SerialName("postCode")
    val postCode: String?,
    @SerialName("postId")
    val postId: Int?,
    @SerialName("postName")
    val postName: String?,
    @SerialName("postUniqueCode")
    val postUniqueCode: String?,
    @SerialName("ranking")
    val ranking: String?
)