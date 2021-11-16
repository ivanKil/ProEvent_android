package ru.myproevent.domain.model.repositories.proevent_login

import android.util.Base64
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import ru.myproevent.domain.model.IProEventDataSource
import ru.myproevent.domain.model.LoginBody
import ru.myproevent.domain.model.SignupBody
import ru.myproevent.domain.model.VerificationBody
import ru.myproevent.domain.model.repositories.local_proevent_user_token.ITokenLocalRepository
import ru.myproevent.domain.model.repositories.local_proevent_user_token.TokenLocalRepository
import javax.inject.Inject

class ProEventLoginRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventLoginRepository {
    private var localToken: String? = null
        set(value) {
            field = value

            if (value == null) {
                tokenLocalRepository.removeTokenFromLocalStorage()
            } else if (rememberMe) {
                tokenLocalRepository.saveTokenInLocalStorage(value)
            }
        }

    private var localEmail: String? = null

    private var localPassword: String? = null

    private var rememberMe = true

    // TODO: вынести в Dagger
    private val tokenLocalRepository: ITokenLocalRepository = TokenLocalRepository()

    override fun getLocalToken(): String? {
        if (localToken != null) {
            return localToken
        }
        localToken = tokenLocalRepository.getTokenOrNull()
        return localToken
    }

    override fun getLocalEmail() = localEmail

    override fun getLocalPassword() = localPassword

    override fun getLocalId(): Long? {
        val token = getLocalToken() ?: return null

        var start = token.indexOf('.') + 1
        var end = token.indexOf('.', start)
        val jSonStr = decodeJWT(token.substring(start, end))

        start = jSonStr.indexOf("id\":") + 4
        end = jSonStr.indexOf(',', start)
        return jSonStr.substring(start, end).toLong()
    }

    // TODO: убрать toLowerCase() для email, когда на сервере пофиксят баг с email чувствительным к регистру
    override fun login(email: String, password: String, rememberMe: Boolean): Completable {
        this.rememberMe = rememberMe
        return api.login(LoginBody(email.toLowerCase(), password))
            .flatMapCompletable { body ->
                this.localToken = body.token
                this.localEmail = email
                this.localPassword = password
                Completable.complete()
            }
            // TODO: вынести Schedulers.io() в Dagger
            .subscribeOn(Schedulers.io())
    }

    override fun logoutFromThisDevice() {
        localToken = null
    }

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

    private fun decodeJWT(str: String): String {
        val decodedBytes: ByteArray = Base64.decode(str, Base64.URL_SAFE)
        return String(decodedBytes, Charsets.UTF_8)
    }
}