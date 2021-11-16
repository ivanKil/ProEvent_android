package ru.myproevent.domain.model.repositories.profiles

import io.reactivex.Completable
import io.reactivex.Single
import ru.myproevent.domain.model.ContactDto
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.domain.model.entities.Contact

interface IProEventProfilesRepository {
    fun saveProfile(profile: ProfileDto) : Completable
    fun getProfile(id: Long) : Single<ProfileDto?>
    fun getContact(contactDto: ContactDto): Single<Contact>
    //fun getQueuedContact(id: Long, status: String): Single<Contact>
}