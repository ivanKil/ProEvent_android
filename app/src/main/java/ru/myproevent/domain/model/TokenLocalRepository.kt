package ru.myproevent.domain.model

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import ru.myproevent.ProEventApp

class TokenLocalRepository : ITokenLocalRepository {
    private val localTokenAlias = "PROEVENT_USER_TOKEN"
    private val tokenPreferencesName = "TOKEN_ENCRYPTED_SHARED_PREFERENCES"
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        tokenPreferencesName,
        masterKeyAlias,
        ProEventApp.instance.applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val editor = sharedPreferences.edit()

    override fun saveTokenInLocalStorage(token: String) {
        editor.putString(localTokenAlias, token)
        editor.apply()
    }

    override fun getTokenOrNull(): String? {
        if (sharedPreferences.contains(localTokenAlias)) {
            return sharedPreferences.getString(localTokenAlias, null)
        }
        return null
    }

    override fun removeTokenFromLocalStorage() {
        editor.remove(localTokenAlias)
        editor.apply()
    }
}