package ru.myproevent.domain.model

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ProEventLoginRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventLoginRepository {

    override fun login(email: String, password: String): Single<String?> {
        return api.login(LoginBody(email, password)).flatMap { body -> Single.just(body.token) }
            .subscribeOn(Schedulers.io())
    }

    override fun refreshCheckCode() {
        TODO("Not yet implemented")
    }
}