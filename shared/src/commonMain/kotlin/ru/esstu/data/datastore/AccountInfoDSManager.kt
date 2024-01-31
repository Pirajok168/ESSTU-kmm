package ru.esstu.data.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import kotlinx.coroutines.flow.Flow
import okio.Path.Companion.toPath
import ru.esstu.data.datastore.model.UserEntity
import ru.esstu.data.datastore.model.UserSerializer
import ru.esstu.data.fileSystem.producePath
import ru.esstu.data.fileSystem.storage


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

    override suspend fun clearUser() {
        dataStore.updateData {
            null
        }
    }

    override val getUser: Flow<UserEntity?> = dataStore.data
}


