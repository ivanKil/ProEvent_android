package ru.myproevent.ui.presenters.authorization

import io.reactivex.Scheduler
import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.domain.model.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class AuthorizationPresenter : BaseMvpPresenter<AuthorizationView>() {

    @Inject
    lateinit var uiScheduler: Scheduler

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

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
            }
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