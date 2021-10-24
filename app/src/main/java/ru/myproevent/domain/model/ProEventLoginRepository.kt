package ru.myproevent.domain.model

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ProEventLoginRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventLoginRepository {
    private var token: String? = null

    private var email: String? = null

    private var password: String? = null

    override fun getToken() = token

    override fun getEmail() = email

    override fun getPassword(): String? {
        TODO("Not yet implemented")
    }

    // TODO: убрать toLowerCase() для email, когда на сервере пофиксят баг с email чувствительным к регистру
    override fun login(email: String, password: String) =
        api.login(LoginBody(email.toLowerCase(), password))
            .flatMapCompletable { body ->
                this.token = body.token
                this.email = email
                this.password = password
                Completable.complete()
            }
            // TODO: вынести Schedulers.io() в Dagger
            .subscribeOn(Schedulers.io())

    // TODO: убрать toLowerCase() для email, когда на сервере пофиксят баг с email чувствительным к регистру
    override fun signup(agreement: Boolean, email: String, password: String) =
        api.signup(SignupBody(agreement, email.toLowerCase(), password))
            .flatMapCompletable { body ->
                this.email = email
                this.password = password
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