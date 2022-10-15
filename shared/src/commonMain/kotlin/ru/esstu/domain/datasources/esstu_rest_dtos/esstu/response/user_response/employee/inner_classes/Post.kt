package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes


data class Post(
    val postCategoryCode: String,
    val postCategoryName: String?,
    val postCode: String?,
    val postId: Int,
    val postName: String,
    val postUniqueCode: String,
    val ranking: String?
)