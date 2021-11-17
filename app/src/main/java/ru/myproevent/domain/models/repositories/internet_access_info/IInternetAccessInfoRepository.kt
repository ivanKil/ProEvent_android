package ru.myproevent.domain.models.repositories.internet_access_info

import io.reactivex.Single

interface IInternetAccessInfoRepository {
    fun hasInternetConnection(): Single<Boolean>
}