package ru.myproevent.domain.model

import android.util.Base64
import android.util.Log
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

    private val alias = "ProEventUserToken"

    // TODO: вынести в Dagger
    private val encryptor = EnCryptor()
    private val decryptor = DeCryptor()

    private fun saveTokenInLocalStorage(token: String) {
        val encryptedText: ByteArray = encryptor.encryptText(alias, token)
        val encryptedString = Base64.encodeToString(encryptedText, Base64.DEFAULT)
        val iv = encryptor.iv
        SettingsRepository.setProperty(
            alias,
            encryptedString,
            iv,
            ProEventApp.instance.applicationContext
        )
    }

    private fun removeTokenFromLocalStorage() {
        TODO("Not yet implemented")
    }

    override fun getLocalToken(): String? {
        if (localToken != null) {
            return localToken
        }
        val encryptedToken =
            SettingsRepository.getProperty(alias, ProEventApp.instance.applicationContext)
        Log.d("[MYLOG]", "encryptedData: ${encryptedToken.data}\n encryptedIv:${encryptedToken.iv}")
        if(encryptedToken.data == null || encryptedToken.iv == null){
            return null
        }
        localToken = decryptor.decryptData(alias, encryptedToken.data!!.toByteArray(), encryptedToken.iv)
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