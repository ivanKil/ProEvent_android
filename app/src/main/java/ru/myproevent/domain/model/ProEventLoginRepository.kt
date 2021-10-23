package ru.myproevent.domain.model

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ProEventLoginRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventLoginRepository {
    private var token: String? = null

    override fun getToken() = token
    
    override fun login(email: String, password: String): Completable {
        return api.login(LoginBody(email, password))
            .flatMapCompletable { body ->
                token = body.token
                Completable.complete()
            }
            .subscribeOn(Schedulers.io())

    }

    override fun refreshCheckCode() {
        TODO("Not yet implemented")
    }
}