package ru.esstu.domain.modules.account.datasources.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.*

import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoBuf.Default.encodeToByteArray
import okio.BufferedSink
import okio.BufferedSource


@Serializable
data class UserEntity(
    val refreshToken: String,
    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val summary: String,
    val photo: String?
)


class UserSerializer: OkioSerializer<UserEntity?>{
    override val defaultValue: UserEntity?
        get() = null


    override suspend fun readFrom(source: BufferedSource): UserEntity? {
        if (source.readByteArray().isEmpty())
            return null
        val map = try {
            ProtoBuf.decodeFromByteArray<UserEntity>(source.readByteArray())
        }catch (e: SerializationException){
            throw CorruptionException("Unable to parse preferences proto.", e)
        }
        return map
    }


    override suspend fun writeTo(t: UserEntity?, sink: BufferedSink) {
        try {
            val byteArray = ProtoBuf.encodeToByteArray(t)
            sink.write(byteArray)
        }catch (e: SerializationException ){
            throw CorruptionException("Unable to parse preferences proto.", e)
        }catch (e: IllegalArgumentException ){
            throw CorruptionException("Unable to parse preferences proto.", e)
        }

    }

}