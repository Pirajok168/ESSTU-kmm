package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.schedule_response


data class ScheduleItem(
    val auditorium: String?,
    val burdenType: LessonType?,
    val groups: List<String?>?,
    val id: Int?,
    val journalId: Any?,
    val lessonNumber: Int?,
    val subject: Subject?,
    val teacher: String?,
    val weekNumber: Int?,
    val weekday: Int?
)