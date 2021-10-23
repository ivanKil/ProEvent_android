package ru.myproevent.domain.model

import io.reactivex.Single

interface IProEventLoginRepository {
    fun login(email: String, password: String): Single<String?>
    fun refreshCheckCode()
}