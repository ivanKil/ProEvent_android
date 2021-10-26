package ru.myproevent.ui.presenters.login

import android.widget.Toast
import io.reactivex.observers.DisposableCompletableObserver
import ru.myproevent.ProEventApp
import ru.myproevent.domain.model.repositories.proevent_login.IProEventLoginRepository
import ru.myproevent.ui.presenters.BaseMvpPresenter
import javax.inject.Inject

class LoginPresenter : BaseMvpPresenter<LoginView>() {
    private inner class LoginObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            router.newRootScreen(screens.home())
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            Toast.makeText(ProEventApp.instance, "${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    @Inject
    lateinit var loginRepository: IProEventLoginRepository

    fun confirmLogin() {
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
            .subscribeWith(LoginObserver())
            .disposeOnDestroy()
    }
}