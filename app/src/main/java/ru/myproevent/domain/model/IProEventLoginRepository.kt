package ru.myproevent.domain.model

import io.reactivex.Completable

interface IProEventLoginRepository {
    fun getToken(): String?
    fun getEmail(): String?
    fun getPassword(): String?
    fun login(email: String, password: String): Completable
    fun signup(agreement: Boolean, email: String, password: String): Completable
    fun verificate(email: String, code: Int): Completable
    fun refreshCheckCode(): Completable
}