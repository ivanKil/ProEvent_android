package ru.myproevent.domain.model.repositories.contacts

import io.reactivex.Completable
import io.reactivex.Single
import ru.myproevent.domain.model.ContactDto
import ru.myproevent.domain.model.Page
import ru.myproevent.domain.model.entities.Status

interface IProEventContactsRepository {
    fun getContacts(page: Int, size: Int, status: Status): Single<Page>
    fun addContact(id: Long): Completable
    fun deleteContact(id: Long): Completable
    fun acceptContact(id: Long): Completable
    fun declineContact(id: Long): Completable
}

