package ru.myproevent.domain.models.repositories.profiles

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import ru.myproevent.domain.models.ContactDto
import ru.myproevent.domain.models.IProEventDataSource
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.entities.Contact
import ru.myproevent.domain.models.entities.Contact.Status
import ru.myproevent.domain.utils.toContact
import java.util.*
import javax.inject.Inject

class ProEventProfilesRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventProfilesRepository {
    override fun getProfile(id: Long): Single<ProfileDto?> = Single.fromCallable {
        Log.d("[CONTACTS]", "getProfile($id) start")
        val response = api.getProfile(id).execute()
        if (response.isSuccessful) {
            return@fromCallable response.body()
        }
        throw HttpException(response)
    }.subscribeOn(Schedulers.io())

    override fun saveProfile(profile: ProfileDto) = Completable.fromCallable {
        val oldProfileResponse = api.getProfile(profile.userId).execute()
        val newProfileResponse = if (oldProfileResponse.isSuccessful) {
            val oldProfile = oldProfileResponse.body()!!
            if (profile.email == null) {
                profile.email = oldProfile.email
            }
            if (profile.fullName == null) {
                profile.fullName = oldProfile.fullName
            }
            if (profile.nickName == null) {
                profile.nickName = oldProfile.nickName
            }
            if (profile.msisdn == null) {
                profile.msisdn = oldProfile.msisdn
            }
            if (profile.position == null) {
                profile.position = oldProfile.position
            }
            if (profile.birthdate == null) {
                profile.birthdate = oldProfile.birthdate
            }
            if (profile.imgUri == null) {
                profile.imgUri = oldProfile.imgUri
            }
            if (profile.description == null) {
                profile.description = oldProfile.description
            }
            api.editProfile(profile).execute()
        } else {
            api.createProfile(profile).execute()
        }
        if (!newProfileResponse.isSuccessful) {
            throw HttpException(newProfileResponse)
        }
        return@fromCallable null
    }.subscribeOn(Schedulers.io())

    override fun getContact(contactDto: ContactDto): Single<Contact> {
        return Single.fromCallable {
            val response = api.getProfile(contactDto.id).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()!!
                    .toContact(Status.fromString(contactDto.status))
            }
            throw HttpException(response)
        }.subscribeOn(Schedulers.io())
    }

}