package ru.myproevent.domain.model.repositories.proevent_login

import io.reactivex.Completable

interface IProEventLoginRepository {
    fun getLocalToken(): String?
    fun getLocalEmail(): String?
    fun getLocalPassword(): String?
    fun login(email: String, password: String): Completable
    fun logoutFromThisDevice()
    fun signup(agreement: Boolean, email: String, password: String): Completable
    fun verificate(email: String, code: Int): Completable
    fun refreshCheckCode(): Completable
}