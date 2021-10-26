package ru.myproevent.domain.model

import io.reactivex.Single

interface IInternetAccessInfoRepository {
    fun hasInternetConnection(): Single<Boolean>
}