package ru.myproevent.domain.model.repositories.profiles

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import ru.myproevent.domain.model.IProEventDataSource
import ru.myproevent.domain.model.ProfileDto
import javax.inject.Inject

class ProEventProfilesRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventProfilesRepository {

    override fun createProfile(profile: ProfileDto): Completable {
        return Completable.fromSingle(api.createProfile(profile)).subscribeOn(Schedulers.io())
    }

    override fun editProfile(profile: ProfileDto): Completable {
        return Completable.fromSingle(api.editProfile(profile)).subscribeOn(Schedulers.io())
    }

    override fun getProfile(id: Long): Completable {
        return Completable.fromSingle(api.getProfile(id)).subscribeOn(Schedulers.io())
    }
}