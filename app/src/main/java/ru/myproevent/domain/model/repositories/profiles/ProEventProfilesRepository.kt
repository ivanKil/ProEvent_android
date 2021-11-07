package ru.myproevent.domain.model.repositories.profiles

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.myproevent.domain.model.ContactDto
import ru.myproevent.domain.model.IProEventDataSource
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.domain.model.entities.Status
import ru.myproevent.utils.toContact
import javax.inject.Inject

class ProEventProfilesRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventProfilesRepository {
    override fun getProfile(id: Long): Single<ProfileDto?> = Single.fromCallable {
        val response = api.getProfile(id).execute()
        if (response.isSuccessful) {
            return@fromCallable response.body()
        }
        throw retrofit2.adapter.rxjava2.HttpException(response)
    }.subscribeOn(Schedulers.io())

    override fun saveProfile(profile: ProfileDto) = Completable.fromCallable {
        val oldProfileResponse = api.getProfile(profile.userId).execute()
        val newProfileResponse = if (oldProfileResponse.isSuccessful) {
            val oldProfile = oldProfileResponse.body()!!
            if(profile.fullName == null){ profile.fullName = oldProfile.fullName }
            if(profile.nickName == null){ profile.nickName = oldProfile.nickName }
            if(profile.msisdn == null){ profile.msisdn = oldProfile.msisdn }
            if(profile.position == null){ profile.position = oldProfile.position }
            if(profile.birthdate == null){ profile.birthdate = oldProfile.birthdate }
            if(profile.imgUri == null){ profile.imgUri = oldProfile.imgUri }
            if(profile.description == null){ profile.description = oldProfile.description }
            api.editProfile(profile).execute()
        } else {
            api.createProfile(profile).execute()
        }
        if (!newProfileResponse.isSuccessful) {
            throw retrofit2.adapter.rxjava2.HttpException(newProfileResponse)
        }
        return@fromCallable null
    }.subscribeOn(Schedulers.io())

    override fun getContact(contactDto: ContactDto) =
        getProfile(contactDto.id).map { it.toContact(Status.fromString(contactDto.status)) }
            .subscribeOn(Schedulers.io())


}