package ru.myproevent.ui.presenters.authorization

import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.domain.model.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class AuthorizationPresenter : BaseMvpPresenter<AuthorizationView>() {
    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    @Inject
    lateinit var interAccessInfoRepository: IInternetAccessInfoRepository

    private inner class LoginObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.newRootScreen(screens.home())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if (error is retrofit2.adapter.rxjava2.HttpException) {
                when (error.code()) {
                    401, 404 -> viewState.authorizationDataInvalid()
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

    fun authorize(email: String, password: String) {
        // TODO: спросить у дизайнера нужено ли здесь отображать progress bar
        loginRepository
            .login(email, password)
            .observeOn(uiScheduler)
            .subscribeWith(LoginObserver())
            .disposeOnDestroy()
    }

    fun openRegistration() {
        router.navigateTo(screens.registration())
    }

    fun recoverPassword() {
        router.navigateTo(screens.recovery())
    }
}