package ru.myproevent.domain.models.repositories.events

import io.reactivex.Completable
import io.reactivex.Single
import ru.myproevent.domain.models.EventDto

interface IProEventEventsRepository {
    fun saveEvent(event: EventDto): Completable
    fun editEvent(event: EventDto): Completable
    fun deleteEvent(event: EventDto): Completable
    fun getEvent(id: Long): Single<EventDto>
    fun getEventsForUser(userId: Long): Single<List<EventDto>>
}

