package ru.myproevent.ui.presenters.registration

import android.widget.Toast
import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.ProEventApp
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class RegistrationPresenter : BaseMvpPresenter<RegistrationView>() {
    private inner class SignupObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.navigateTo(screens.code())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if (error is retrofit2.adapter.rxjava2.HttpException) {
                if (error.code() == 409) {
                    Toast.makeText(
                        ProEventApp.instance,
                        "Для введённого email уже есть аккаунт",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                return
            }
            interAccessInfoRepository
                .hasInternetConnection()
                .observeOn(uiScheduler)
                .subscribeWith(InterAccessInfoObserver(error.message))
                .disposeOnDestroy()
        }
    }

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    @Inject
    lateinit var interAccessInfoRepository: IInternetAccessInfoRepository

    fun signup() {
        router.navigateTo(screens.authorization())
    }

    fun continueRegistration(agreement: Boolean, email: String, password: String) {
        // TODO: спросить у дизайнера нужен ли progress bar
        loginRepository
            .signup(agreement, email, password)
            .observeOn(uiScheduler)
            .subscribeWith(SignupObserver()).disposeOnDestroy()
    }
}