package ru.myproevent.ui.presenters.login

import android.widget.Toast
import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.ProEventApp
import ru.myproevent.domain.model.ProfileDto
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.domain.model.repositories.profiles.IProEventProfilesRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class LoginPresenter : BaseMvpPresenter<LoginView>() {
    private inner class AuthObserver(val login: String) : DisposableCompletableObserver() {
        override fun onComplete() {
            profileRepository
                .saveProfile(
                    ProfileDto(
                        userId = loginRepository.getLocalId()!!,
                        nickName = login
                    )
                )
                .observeOn(uiScheduler)
                .subscribeWith(LoginSaveObserver())
                .disposeOnDestroy()
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            interAccessInfoRepository
                .hasInternetConnection()
                .observeOn(uiScheduler)
                .subscribeWith(InterAccessInfoObserver(error.message))
                .disposeOnDestroy()
        }
    }

    private inner class LoginSaveObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.newRootScreen(screens.home())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            interAccessInfoRepository
                .hasInternetConnection()
                .observeOn(uiScheduler)
                .subscribeWith(InterAccessInfoObserver(error.message))
                .disposeOnDestroy()
        }
    }

    @Inject
    lateinit var interAccessInfoRepository: IInternetAccessInfoRepository

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    @Inject
    lateinit var profileRepository: IProEventProfilesRepository

    fun confirmLogin(login: String) {
        if (loginRepository.getLocalEmail() == null || loginRepository.getLocalPassword() == null) {
            Toast.makeText(
                ProEventApp.instance,
                "Этого не должно было произойти: loginRepository.getLocalEmail() == null || loginRepository.getLocalPassword() == null",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        // TODO: уточнить у дизайнера нужен ли progress_bar
        loginRepository
            .login(loginRepository.getLocalEmail()!!, loginRepository.getLocalPassword()!!)
            .observeOn(uiScheduler)
            .subscribeWith(AuthObserver(login))
            .disposeOnDestroy()
    }
}