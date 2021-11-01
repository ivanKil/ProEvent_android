package ru.myproevent.domain.model.repositories.profiles

import io.reactivex.Completable
import io.reactivex.Single
import ru.myproevent.domain.model.ProfileDto

interface IProEventProfilesRepository {
    fun createProfile(profile: ProfileDto) : Completable
    fun editProfile(profile: ProfileDto) : Completable
    fun getProfile(id: Long) : Single<ProfileDto>
}