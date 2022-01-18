package ru.myproevent.ui.presenters.settings.account

import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.myproevent.domain.models.ProfileDto
import ru.myproevent.domain.models.repositories.images.IImagesRepository
import ru.myproevent.domain.models.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.models.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.domain.models.repositories.profiles.IProEventProfilesRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import java.io.File
import javax.inject.Inject

class AccountPresenter(localRouter: Router) : BaseMvpPresenter<AccountView>(localRouter) {
    private inner class ProfileEditObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            viewState.showMessage("Изменения сохранены")
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            interAccessInfoRepository
                .hasInternetConnection()
                .observeOn(uiScheduler)
                .subscribeWith(InterAccessInfoObserver("Этого не должно было произойти(ProfileEditObserver):\n${error}"))
                .disposeOnDestroy()
        }
    }

    private inner class ProfileGetObserver : DisposableSingleObserver<ProfileDto>() {
        override fun onSuccess(profileDto: ProfileDto) {
            viewState.showProfile(profileDto)
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if (error is retrofit2.adapter.rxjava2.HttpException) {
                when (error.code()) {
                    404 -> viewState.makeProfileEditable()
                }
                return
            }
            interAccessInfoRepository
                .hasInternetConnection()
                .observeOn(uiScheduler)
                .subscribeWith(InterAccessInfoObserver("Этого не должно было произойти (ProfileGetObserver):\n${error}"))
                .disposeOnDestroy()
        }
    }

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    @Inject
    lateinit var profilesRepository: IProEventProfilesRepository

    @Inject
    lateinit var interAccessInfoRepository: IInternetAccessInfoRepository

    @Inject
    lateinit var imagesRepository: IImagesRepository

    fun saveProfile(
        name: String,
        phone: String,
        dateOfBirth: String,
        position: String,
        role: String
    ) {
        profilesRepository
            .saveProfile(
                ProfileDto(
                    userId = loginRepository.getLocalId()!!,
                    fullName = name,
                    msisdn = phone,
                    position = position,
                    birthdate = dateOfBirth,
                    description = role
                )
            )
            .observeOn(uiScheduler)
            .subscribeWith(ProfileEditObserver())
            .disposeOnDestroy()
    }

    fun getProfile() {
        profilesRepository
            .getProfile(loginRepository.getLocalId()!!)
            .observeOn(uiScheduler)
            .subscribeWith(ProfileGetObserver())
            .disposeOnDestroy()
    }

    fun saveImage(file: File) {
        val call = imagesRepository.saveImage(file)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, t.printStackTrace().toString())

            }
        })
    }

    fun handleImage(uuid: String) {

    }

    companion object {
        const val TAG = "AccountPresenter"
    }
}