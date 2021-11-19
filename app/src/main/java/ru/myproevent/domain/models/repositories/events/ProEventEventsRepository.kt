package ru.myproevent.domain.models.repositories.events

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.myproevent.domain.models.EventDto
import ru.myproevent.domain.models.IProEventDataSource
import javax.inject.Inject

class ProEventEventsRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventEventsRepository {

    override fun saveEvent(event: EventDto): Completable {
        return Completable.fromSingle(api.saveEvent(event).subscribeOn(Schedulers.io()))
    }

    override fun editEvent(event: EventDto): Completable {
        return Completable.fromSingle(api.editEvent(event).subscribeOn(Schedulers.io()))
    }

    override fun deleteEvent(event: EventDto): Completable {
        return api.deleteEvent(event).subscribeOn(Schedulers.io())
    }

    override fun getEvent(id: Long): Single<EventDto> {
        return api.getEvent(id).subscribeOn(Schedulers.io())
    }

    override fun getEventsForUser(userId: Long): Single<List<EventDto>> {
        return api.getEventsForUser(userId).subscribeOn(Schedulers.io())
    }

}