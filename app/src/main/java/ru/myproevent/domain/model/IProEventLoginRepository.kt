package ru.myproevent.domain.model

import io.reactivex.Completable

interface IProEventLoginRepository {
    fun getToken(): String?
    fun login(email: String, password: String): Completable
    fun refreshCheckCode()
}