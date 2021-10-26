package ru.myproevent.domain.model.repositories.internet_access_info

import io.reactivex.Single

interface IInternetAccessInfoRepository {
    fun hasInternetConnection(): Single<Boolean>
}