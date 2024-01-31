package ru.esstu.features.profile.porfolio.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface EmployeePortfolio {
    @Serializable
    data class EmployeeEducation(
        @SerialName("creationTime")
        val creationTime: Long?,
        @SerialName("educationType")
        val educationType: EducationType?,
        @SerialName("employeeId")
        val employeeId: Int?,
        @SerialName("fileCode")
        val fileCode: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("institution")
        val institution: Institution?,
        @SerialName("modificationTime")
        val modificationTime: Long?,
        @SerialName("note")
        val note: String?,
        @SerialName("number")
        val number: String?,
        @SerialName("qualification")
        val qualification: Qualification?,
        @SerialName("receiptDate")
        val receiptDate: String?,
        @SerialName("series")
        val series: String?,
        @SerialName("speciality")
        val speciality: Speciality?,
        @SerialName("specialityCode")
        val specialityCode: String?,
        @SerialName("status")
        val status: String?
    ) : EmployeePortfolio {
        @Serializable
        data class EducationType(
            @SerialName("code")
            val code: String?,
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?
        )

        @Serializable
        data class Institution(
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?,
            @SerialName("shortName")
            val shortName: String?
        )

        @Serializable
        data class Qualification(
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?
        )

        @Serializable
        data class Speciality(
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?
        )
    }

    @Serializable
    data class AddEducation(
        @SerialName("creationTime")
        val creationTime: Long?,
        @SerialName("employeeId")
        val employeeId: Int?,
        @SerialName("endDate")
        val endDate: String?,
        @SerialName("fileCode")
        val fileCode: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("internal")
        val `internal`: Boolean?,
        @SerialName("length")
        val length: Int?,
        @SerialName("modificationTime")
        val modificationTime: Long?,
        @SerialName("name")
        val name: String?,
        @SerialName("number")
        val number: String?,
        @SerialName("place")
        val place: String?,
        @SerialName("receiptDate")
        val receiptDate: String?,
        @SerialName("series")
        val series: String?,
        @SerialName("startDate")
        val startDate: String?,
        @SerialName("status")
        val status: String?,
        @SerialName("type")
        val type: Type?
    ) : EmployeePortfolio {
        @Serializable
        data class Type(
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?
        )
    }

    @Serializable
    data class Award(
        @SerialName("awardDate")
        val awardDate: String?,
        @SerialName("awardName")
        val awardName: String?,
        @SerialName("creationTime")
        val creationTime: Long?,
        @SerialName("employeeId")
        val employeeId: Int?,
        @SerialName("fileCode")
        val fileCode: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("modificationTime")
        val modificationTime: Long?,
        @SerialName("number")
        val number: String?,
        @SerialName("organization")
        val organization: Organization?,
        @SerialName("scale")
        val scale: Scale?,
        @SerialName("series")
        val series: String?,
        @SerialName("status")
        val status: String?,
        @SerialName("type")
        val type: Type?
    ) : EmployeePortfolio {
        @Serializable
        data class Organization(
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?,
            @SerialName("ranking")
            val ranking: Int?,
            @SerialName("shortName")
            val shortName: String?
        )

        @Serializable
        data class Scale(
            @SerialName("code")
            val code: String?,
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?,
            @SerialName("ranking")
            val ranking: Int?
        )

        @Serializable
        data class Type(
            @SerialName("code")
            val code: String?,
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?,
            @SerialName("ranking")
            val ranking: Int?
        )
    }
}