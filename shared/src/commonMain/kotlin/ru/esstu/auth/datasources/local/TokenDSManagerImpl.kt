package ru.esstu.auth.datasources.local


import com.russhwolf.settings.Settings


class TokenDSManagerImpl constructor(
    private val authDataStore: Settings
) : ITokenDSManager {
    private object PreferencesKeys {
        const val SESSION_TOKEN_KEY = "SESSION_TOKEN_KEY"
        const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        const val TOKEN_TYPE_KEY = "TOKEN_TYPE_KEY"
        const val USER_TYPE_KEY = "USER_TYPE_KEY"
    }

    override fun getAccessToken(): TokenPair? {
        val sessionToken: String =
            authDataStore.getStringOrNull(PreferencesKeys.SESSION_TOKEN_KEY) ?: return null
        val refreshToken: String =
            authDataStore.getStringOrNull(PreferencesKeys.REFRESH_TOKEN_KEY) ?: return null
        val userType: String =
            authDataStore.getStringOrNull(PreferencesKeys.USER_TYPE_KEY) ?: return null
        val tokenType: String =
            authDataStore.getStringOrNull(PreferencesKeys.TOKEN_TYPE_KEY) ?: return null
        return TokenPair(
            tokenType = tokenType,
            refreshToken = refreshToken,
            userType = userType,
            accessToken = sessionToken
        )
    }


    override suspend fun getToken(): TokenPair? {
        val sessionToken: String =
            authDataStore.getStringOrNull(PreferencesKeys.SESSION_TOKEN_KEY) ?: return null
        val refreshToken: String =
            authDataStore.getStringOrNull(PreferencesKeys.REFRESH_TOKEN_KEY) ?: return null
        val userType: String =
            authDataStore.getStringOrNull(PreferencesKeys.USER_TYPE_KEY) ?: return null
        val tokenType: String =
            authDataStore.getStringOrNull(PreferencesKeys.TOKEN_TYPE_KEY) ?: return null
        return TokenPair(
            tokenType = tokenType,
            refreshToken = refreshToken,
            userType = userType,
            accessToken = sessionToken
        )
    }


    override suspend fun setToken(tokens: TokenPair?) {
        if (tokens != null) {
            authDataStore.putString(PreferencesKeys.SESSION_TOKEN_KEY, tokens.accessToken)
            authDataStore.putString(PreferencesKeys.REFRESH_TOKEN_KEY, tokens.refreshToken)
            authDataStore.putString(PreferencesKeys.USER_TYPE_KEY, tokens.userType)
            authDataStore.putString(PreferencesKeys.TOKEN_TYPE_KEY, tokens.tokenType)
        } else {
            authDataStore.clear()
        }
    }

}