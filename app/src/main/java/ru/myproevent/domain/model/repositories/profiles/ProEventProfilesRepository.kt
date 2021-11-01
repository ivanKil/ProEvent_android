package ru.myproevent.domain.model.repositories.profiles

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.myproevent.domain.model.IProEventDataSource
import ru.myproevent.domain.model.ProfileDto
import javax.inject.Inject

class ProEventProfilesRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventProfilesRepository {

    private fun createProfile(profile: ProfileDto): Completable {
        return Completable.fromSingle(api.createProfile(profile)).subscribeOn(Schedulers.io())
    }

    private fun editProfile(profile: ProfileDto): Completable {
        return Completable.fromSingle(api.editProfile(profile)).subscribeOn(Schedulers.io())
    }

    override fun getProfile(id: Long): Single<ProfileDto?> = Single.fromCallable {
        val response = api.getProfile(id).execute()
        if (response.isSuccessful) {
            return@fromCallable response.body()
        }
        throw retrofit2.adapter.rxjava2.HttpException(response)
    }.subscribeOn(Schedulers.io())

    override fun saveProfile(profile: ProfileDto) = Completable.fromCallable {
        api.getProfile(profile.userId).execute().also { response ->
            return@fromCallable if (response.isSuccessful) {
                editProfile(profile)
            } else if (response.code() == 404) {
                createProfile(profile)
            } else {
                Completable.error(Throwable(response.errorBody().toString()))
            }
        }
    }.subscribeOn(Schedulers.io())
}