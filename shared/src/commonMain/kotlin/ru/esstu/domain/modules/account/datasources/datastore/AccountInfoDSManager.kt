package ru.esstu.domain.modules.account.datasources.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


import kotlinx.coroutines.flow.Flow
import okio.*
import okio.Path.Companion.toPath



interface _FileSystem{
    val fileSystem: FileSystem

    val path: String
}

expect fun storage(): _FileSystem

expect fun producePath(): _FileSystem

fun create(): DataStore<UserEntity?> {
    return DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = storage().fileSystem,
            serializer = UserSerializer(),
            producePath = { producePath().path.toPath() }
        ),
    )
}

internal const val dataStoreFileName = "dice.preferences_pb"


class AccountInfoDSManager(
    private val dataStore: DataStore<UserEntity?>
) : IAccountInfoDSManager {



     override suspend fun setUser(user: UserEntity) {

         dataStore.updateData {
             user
         }
    }

    override suspend fun clearUser(){
        dataStore.updateData {
            null
        }
    }

    override val getUser: Flow<UserEntity?> = dataStore.data
}


