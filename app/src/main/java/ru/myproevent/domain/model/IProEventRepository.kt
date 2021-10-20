package ru.myproevent.domain.model

interface IProEventRepository {
    fun getToken(loginBody: LoginBody): String?
}