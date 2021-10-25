package ru.myproevent.domain.model

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProEventLoginRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventLoginRepository {
    private var localToken: String? = null
        set(value) {
            if (value == null) {
                tokenLocalRepository.removeTokenFromLocalStorage()
            } else {
                tokenLocalRepository.saveTokenInLocalStorage(value)
            }
            field = value
        }

    private var localEmail: String? = null

    private var localPassword: String? = null

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
}