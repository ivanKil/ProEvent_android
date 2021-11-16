package ru.myproevent.domain.model.repositories.profiles

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import ru.myproevent.domain.model.ContactDto
import ru.myproevent.domain.model.IProEventDataSource
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.domain.model.entities.Contact
import ru.myproevent.domain.model.entities.Status
import ru.myproevent.utils.toContact
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

//    // TODO: рефакторинг: пределать это так, чтобы использовались только средства RXJava
//    private val single = Executors.newSingleThreadExecutor()
//    override fun getQueuedContact(id: Long, status: String): Single<Contact> {
//        val returnValue = single.submit(Callable<Single<Contact>> {
//            Single.fromCallable {
//                Log.d("[CONTACTS]", "getProfile($id) start")
//                // TODO: исправить баг приводящий к вылету приложения, если эта функция не успевает выполниться вовремя(до того как будет совершенно нажатие на элемент из списка контактов)
//                // Thread.sleep(2000)
//                val response = api.getProfile(id).execute()
//                if (response.isSuccessful) {
//                    return@fromCallable response.body()!!.toContact(Status.fromString(status))
//                }
//                throw retrofit2.adapter.rxjava2.HttpException(response)
//            }
//        }).get()
//        Log.d("[CONTACTS]", "getProfile($id) finish")
//        return returnValue
//    }

    override fun saveProfile(profile: ProfileDto) = Completable.fromCallable {
        val oldProfileResponse = api.getProfile(profile.userId).execute()
        val newProfileResponse = if (oldProfileResponse.isSuccessful) {
            val oldProfile = oldProfileResponse.body()!!
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