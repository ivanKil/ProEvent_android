package ru.myproevent.domain.model

interface ITokenLocalRepository {
    fun saveTokenInLocalStorage(token: String)
    fun getTokenOrNull(): String?
    fun removeTokenFromLocalStorage()
}