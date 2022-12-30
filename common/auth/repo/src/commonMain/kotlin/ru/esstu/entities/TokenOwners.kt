package ru.esstu.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class TokenOwners {
    data class Teacher(val id: String) : TokenOwners()
    data class Student(val id: String) : TokenOwners()
    object Entrant : TokenOwners()
    object Guest : TokenOwners()

    //region сереализация
    fun asString(): String {
        val name = this::class.simpleName
        return when (this) {
            is Guest, is Entrant -> name ?: ""
            is Student -> "$name|$id"
            is Teacher -> "$name|$id"
        }
    }

    companion object {
        fun fromString(owner: String): TokenOwners? {

            val name = owner.split("|").first()
            val id = owner.split("|").last()
            return when (name) {
                Teacher::class.simpleName -> Teacher(id)
                Student::class.simpleName -> Student(id)
                Entrant::class.simpleName -> Entrant
                Guest::class.simpleName -> Guest
                else -> null
            }
        }
    }
    //endregion
}