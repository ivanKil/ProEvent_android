package ru.myproevent.domain.models.repositories.local_proevent_user_token

interface ITokenLocalRepository {
    fun saveTokenInLocalStorage(token: String)
    fun getTokenOrNull(): String?
    fun removeTokenFromLocalStorage()
}