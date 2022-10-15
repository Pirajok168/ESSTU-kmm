package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.educational_process_response

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.educational_process_response.inner_classes.DisciplineBean
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.educational_process_response.inner_classes.DrsBean
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.educational_process_response.inner_classes.ProfileBean


data class UserEducationProcessResponse(
    val accelerated: Any,
    val code: String,
    val competenceStatus: String,
    val countMonth: Int,
    val countPereodicity: Any,
    val countPeriodsOfStudy: Any,
    val dirCode: String,
    val directionCode: Any,
    val directionName: Any,
    val disciplineBeanList: List<DisciplineBean>,
    val disciplineFileWpBeans: Any,
    val dlfCode: String,
    val drsBean: List<DrsBean>,
    val eduForm: String,
    val eduFormCode: Any,
    val eduFormName: Any,
    val eduLevelCode: Any,
    val eduLevelName: Any,
    val educationStartDate: Long,
    val id: Int,
    val lvlCode: Any,
    val mapDiscipline: Any,
    val mapProfile: Any,
    val name: String,
    val profileBeanList: List<ProfileBean>,
    val remote: Any,
    val standart: Any,
    val status: String,
    val storageCodeDocOOP: String,
    val storageCodeDocOOPA: String,
    val storageCodeDocUP: String,
    val storageCodeDocs: Any,
    val subjectCode: Any,
    val year: Any
)