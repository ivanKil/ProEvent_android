package ru.myproevent.ui.presenters.authorization.code

import android.widget.Toast
import com.github.terrakok.cicerone.Router
import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.ProEventApp
import ru.myproevent.domain.models.repositories.internet_access_info.IInternetAccessInfoRepository
import ru.myproevent.domain.models.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class CodePresenter(localRouter: Router) : BaseMvpPresenter<CodeView>(localRouter) {
    private inner class VerificationObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            localRouter.newRootScreen(screens.login())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            if (error is retrofit2.adapter.rxjava2.HttpException) {
                if(error.code() == 401){
                    Toast.makeText(ProEventApp.instance, "Неверный код", Toast.LENGTH_LONG)
                        .show()
                    return
                }
                Toast.makeText(ProEventApp.instance, "Ошибка ${error.code()}", Toast.LENGTH_LONG)
                    .show()
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

    fun continueRegistration(code: Int) {
        // TODO: спросить у дизайнера нужено ли здесь отображать progress bar
        loginRepository
            .verificate(loginRepository.getLocalEmail()!!, code)
            .observeOn(uiScheduler)
            .subscribeWith(VerificationObserver())
            .disposeOnDestroy()
    }

    fun authorize() {
        localRouter.navigateTo(screens.authorization())
    }

    fun getEmail() = loginRepository.getLocalEmail()
}