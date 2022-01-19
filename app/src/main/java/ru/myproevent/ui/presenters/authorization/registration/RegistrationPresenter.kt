package ru.myproevent.ui.presenters.authorization.registration

import com.github.terrakok.cicerone.Router
import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.domain.models.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.models.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class RegistrationPresenter(localRouter: Router) : BaseMvpPresenter<RegistrationView>(localRouter) {
    private inner class SignupObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            localRouter.navigateTo(screens.code())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if (error is retrofit2.adapter.rxjava2.HttpException) {
                if (error.code() == 409) {
                    viewState.showMessage("Для введённого email уже есть аккаунт")
                    viewState.showEmailErrorMessage("Для введённого email уже есть аккаунт")
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
        localRouter.navigateTo(screens.authorization())
    }

    fun continueRegistration(agreement: Boolean, email: String, password: String, confirmedPassword: String) {
        // TODO: спросить у дизайнера нужен ли progress bar
        var errorMessage: String? = ""
        if (password != confirmedPassword) {
            errorMessage += "Пароли не совпадают.\n"
            viewState.showPasswordConfirmErrorMessage("Пароли не совпадают.\n")
        }
        if (email.isEmpty()) {
            errorMessage += "Поле с email не может быть пустым."
            viewState.showEmailErrorMessage("Поле с email не может быть пустым.")
        }
        if(!errorMessage.isNullOrBlank()){
            viewState.showMessage(errorMessage)
            return
        }
        loginRepository
            .signup(agreement, email, password)
            .observeOn(uiScheduler)
            .subscribeWith(SignupObserver()).disposeOnDestroy()
    }

    fun emailEdited() {
        viewState.showEmailErrorMessage(null)
    }

    fun passwordEdited() {
        viewState.showPasswordErrorMessage(null)
    }

    fun passwordConfirmEdited(){
        viewState.showPasswordConfirmErrorMessage(null)
    }
}