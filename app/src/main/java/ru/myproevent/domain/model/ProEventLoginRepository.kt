package ru.myproevent.domain.model

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import ru.myproevent.ProEventApp
import javax.inject.Inject


class ProEventLoginRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventLoginRepository {
    private var localToken: String? = null
        set(value) {
            if (value == null) {
                removeTokenFromLocalStorage()
            } else {
                saveTokenInLocalStorage(value)
            }
            field = value
        }

    private var localEmail: String? = null

    private var localPassword: String? = null

    private val localTokenAlias = "PROEVENT_USER_TOKEN"
    private val tokenPreferencesName = "TOKEN_ENCRYPTED_SHARED_PREFERENCES"

    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val sharedPreferences = EncryptedSharedPreferences.create(
        tokenPreferencesName,
        masterKeyAlias,
        ProEventApp.instance.applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val editor = sharedPreferences.edit()

    private fun saveTokenInLocalStorage(token: String) {
        editor.putString(localTokenAlias, token);
        editor.apply();
    }

    private fun removeTokenFromLocalStorage() {
        editor.remove(localTokenAlias)
    }

    override fun getLocalToken(): String? {
        if (localToken != null) {
            return localToken
        }
        if(sharedPreferences.contains(localTokenAlias)) {
            localToken = sharedPreferences.getString(localTokenAlias, null)
        }
        return localToken
    }

    override fun getLocalEmail() = localEmail

    override fun getLocalPassword() = localPassword

    // TODO: убрать toLowerCase() для email, когда на сервере пофиксят баг с email чувствительным к регистру
    override fun login(email: String, password: String) =
        api.login(LoginBody(email.toLowerCase(), password))
            .flatMapCompletable { body ->
                this.localToken = body.token
                this.localEmail = email
                this.localPassword = password
                Completable.complete()
            }
            // TODO: вынести Schedulers.io() в Dagger
            .subscribeOn(Schedulers.io())

    // TODO: убрать toLowerCase() для email, когда на сервере пофиксят баг с email чувствительным к регистру
    override fun signup(agreement: Boolean, email: String, password: String) =
        api.signup(SignupBody(agreement, email.toLowerCase(), password))
            .flatMapCompletable { body ->
                this.localEmail = email
                this.localPassword = password
                Completable.complete()
            }
            .subscribeOn(Schedulers.io())

    // TODO: убрать toLowerCase() для email, когда на сервере пофиксят баг с email чувствительным к регистру
    override fun verificate(email: String, code: Int) =
        api.verificate(VerificationBody(code, email.toLowerCase()))
            .subscribeOn(Schedulers.io())

    override fun refreshCheckCode(): Completable {
        TODO("Not yet implemented")
    }
}